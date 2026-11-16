package com.DannaS2.LogiTrack.service.impl;

import com.DannaS2.LogiTrack.dto.request.ProductoDtoRequest;
import com.DannaS2.LogiTrack.dto.response.ProductoDtoResponse;
import com.DannaS2.LogiTrack.exception.RecursoNoEncontradoException;
import com.DannaS2.LogiTrack.mapper.ProductoMapper;
import com.DannaS2.LogiTrack.model.*;
import com.DannaS2.LogiTrack.repository.MarcaRepository;
import com.DannaS2.LogiTrack.repository.ProductoRepository;
import com.DannaS2.LogiTrack.service.ProductoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final MarcaRepository marcaRepository;
    private final ProductoMapper productoMapper;
    private final AuditoriaRegistroService auditoriaRegistroService;

    public ProductoServiceImpl(ProductoRepository productoRepository,
                               MarcaRepository marcaRepository,
                               ProductoMapper productoMapper,
                               AuditoriaRegistroService auditoriaRegistroService) {
        this.productoRepository = productoRepository;
        this.marcaRepository = marcaRepository;
        this.productoMapper = productoMapper;
        this.auditoriaRegistroService = auditoriaRegistroService;
    }

    @Override
    @Transactional
    public ProductoDtoResponse guardar(ProductoDtoRequest dto) {
        Marca marca = buscarMarca(dto.marcaId());
        Producto guardado = productoRepository.save(productoMapper.DtoToEntity(dto, marca));
        ProductoDtoResponse response = productoMapper.entityToDto(guardado);
        auditoriaRegistroService.registrar(com.DannaS2.LogiTrack.model.TipoOperacion.INSERT, "PRODUCTO", guardado.getId(), null, response);
        return response;
    }

    @Override
    @Transactional
    public ProductoDtoResponse actualizar(ProductoDtoRequest dto, Long id) {
        Producto producto = buscarEntidad(id);
        Marca marca = buscarMarca(dto.marcaId());
        ProductoDtoResponse anterior = productoMapper.entityToDto(producto);
        productoMapper.updateEntityToDto(producto, dto, marca);
        ProductoDtoResponse actualizado = productoMapper.entityToDto(producto);
        auditoriaRegistroService.registrar(com.DannaS2.LogiTrack.model.TipoOperacion.UPDATE, "PRODUCTO", producto.getId(), anterior, actualizado);
        return actualizado;
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Producto producto = buscarEntidad(id);
        ProductoDtoResponse anterior = productoMapper.entityToDto(producto);
        productoRepository.delete(producto);
        auditoriaRegistroService.registrar(com.DannaS2.LogiTrack.model.TipoOperacion.DELETE, "PRODUCTO", id, anterior, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDtoResponse> listar() {
        return productoRepository.findAll().stream().map(productoMapper::entityToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoDtoResponse buscarId(Long id) {
        return productoMapper.entityToDto(buscarEntidad(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDtoResponse> buscarCategoria(Categoria categoria) {
        return productoRepository.findByCategoria(categoria).stream().map(productoMapper::entityToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDtoResponse> buscarNombre(String texto) {
        return productoRepository.findByNombreContainingIgnoreCase(texto).stream()
                .map(productoMapper::entityToDto).toList();
    }

    private Producto buscarEntidad(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado: " + id));
    }

    private Marca buscarMarca(Long id) {
        return marcaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Marca no encontrada: " + id));
    }
}
