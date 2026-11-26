package com.DannaS2.LogiTrack.service.impl;

import com.DannaS2.LogiTrack.dto.request.BodegaDtoRequest;
import com.DannaS2.LogiTrack.dto.response.BodegaDtoResponse;
import com.DannaS2.LogiTrack.exception.RecursoDuplicadoException;
import com.DannaS2.LogiTrack.exception.RecursoNoEncontradoException;
import com.DannaS2.LogiTrack.mapper.BodegaMapper;
import com.DannaS2.LogiTrack.model.Bodega;
import com.DannaS2.LogiTrack.model.Ciudad;
import com.DannaS2.LogiTrack.model.Persona;
import com.DannaS2.LogiTrack.repository.BodegaRepository;
import com.DannaS2.LogiTrack.repository.CiudadRepository;
import com.DannaS2.LogiTrack.repository.PersonaRepository;
import com.DannaS2.LogiTrack.service.AuditoriaService;
import com.DannaS2.LogiTrack.service.BodegaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.DannaS2.LogiTrack.repository.EmpleadoRepository;
import com.DannaS2.LogiTrack.repository.InventarioBodegaRepository;
import com.DannaS2.LogiTrack.repository.MovimientoRepository;

import java.util.List;

@Service
public class BodegaServiceImpl implements BodegaService {

    private final BodegaRepository bodegaRepository;
    private final CiudadRepository ciudadRepository;
    private final PersonaRepository personaRepository;
    private final BodegaMapper bodegaMapper;
    private final AuditoriaService auditoriaService;
    private final EmpleadoRepository empleadoRepository;
    private final InventarioBodegaRepository inventarioBodegaRepository;
    private final MovimientoRepository movimientoRepository;

    public BodegaServiceImpl(
            BodegaRepository bodegaRepository,
            CiudadRepository ciudadRepository,
            PersonaRepository personaRepository,
            BodegaMapper bodegaMapper,
            AuditoriaService auditoriaService,
            EmpleadoRepository empleadoRepository,
            InventarioBodegaRepository inventarioBodegaRepository,
            MovimientoRepository movimientoRepository
    ) {
        this.bodegaRepository = bodegaRepository;
        this.ciudadRepository = ciudadRepository;
        this.personaRepository = personaRepository;
        this.bodegaMapper = bodegaMapper;
        this.auditoriaService = auditoriaService;
        this.empleadoRepository = empleadoRepository;
        this.inventarioBodegaRepository = inventarioBodegaRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Override
    @Transactional
    public BodegaDtoResponse guardar(BodegaDtoRequest dto) {
        Ciudad ciudad = obtenerCiudad(dto.ciudadId());
        if (bodegaRepository.existsByNombreIgnoreCaseAndCiudadId(dto.nombre(), ciudad.getId())) {
            throw new RecursoDuplicadoException("Ya existe una bodega con ese nombre en esa ciudad");
        }
        Persona encargado = obtenerEncargado(dto.encargadoId());
        Bodega bodega = bodegaMapper.DtoToEntity(dto, ciudad, encargado);
        Bodega guardada = bodegaRepository.save(bodega);
        BodegaDtoResponse response = bodegaMapper.entityToDto(guardada);
        auditoriaService.registrar(com.DannaS2.LogiTrack.model.TipoOperacion.INSERT, "BODEGA", guardada.getId(), null, response);
        return response;
    }

    @Override
    @Transactional
    public BodegaDtoResponse actualizar(BodegaDtoRequest dto, Long id) {
        Bodega bodega = buscarEntidad(id);
        Ciudad ciudad = obtenerCiudad(dto.ciudadId());
        Persona encargado = obtenerEncargado(dto.encargadoId());
        BodegaDtoResponse anterior = bodegaMapper.entityToDto(bodega);
        bodegaMapper.updateEntityToDto(bodega, dto, ciudad, encargado);
        BodegaDtoResponse actualizada = bodegaMapper.entityToDto(bodega);
        auditoriaService.registrar(com.DannaS2.LogiTrack.model.TipoOperacion.UPDATE, "BODEGA", bodega.getId(), anterior, actualizada);
        return actualizada;
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Bodega bodega = buscarEntidad(id);

        // Verificar empleados asignados
        if (!empleadoRepository.findByBodegaId(id).isEmpty()) {
            throw new RecursoDuplicadoException(
                    "No se puede eliminar la bodega porque tiene empleados asignados"
            );
        }

        // Verificar inventario registrado
        if (!inventarioBodegaRepository.findByBodegaId(id).isEmpty()) {
            throw new RecursoDuplicadoException(
                    "No se puede eliminar la bodega porque tiene inventario registrado"
            );
        }

        // Verificar movimientos relacionados
        if (!movimientoRepository.findByBodegaOrigenId(id).isEmpty()
                || !movimientoRepository.findByBodegaDestinoId(id).isEmpty()) {
            throw new RecursoDuplicadoException(
                    "No se puede eliminar la bodega porque tiene movimientos asociados"
            );
        }

        BodegaDtoResponse anterior = bodegaMapper.entityToDto(bodega);

        bodegaRepository.delete(bodega);

        auditoriaService.registrar(
                com.DannaS2.LogiTrack.model.TipoOperacion.DELETE,
                "BODEGA",
                id,
                anterior,
                null
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<BodegaDtoResponse> listar() {
        return bodegaRepository.findAll().stream().map(bodegaMapper::entityToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BodegaDtoResponse buscarId(Long id) {
        return bodegaMapper.entityToDto(buscarEntidad(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BodegaDtoResponse> buscarCiudad(Long ciudadId) {
        return bodegaRepository.findByCiudadId(ciudadId).stream().map(bodegaMapper::entityToDto).toList();
    }

    private Bodega buscarEntidad(Long id) {
        return bodegaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Bodega no encontrada: " + id));
    }

    private Ciudad obtenerCiudad(Long id) {
        return ciudadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ciudad no encontrada: " + id));
    }   // <-- esta llave era la que faltaba

    // El encargado es opcional: si no llega encargadoId, la bodega queda sin encargado
    private Persona obtenerEncargado(Long encargadoId) {
        if (encargadoId == null) return null;
        return personaRepository.findById(encargadoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Encargado no encontrado: " + encargadoId));
    }

    //EXAMEN
    @Override
    @Transactional(readOnly = true)
    public List<BodegaDtoResponse> buscarEncargado(Long encargadoId) {
        return bodegaRepository.findByEncargadoId(encargadoId).stream().map(bodegaMapper::entityToDto).toList();
    }
}