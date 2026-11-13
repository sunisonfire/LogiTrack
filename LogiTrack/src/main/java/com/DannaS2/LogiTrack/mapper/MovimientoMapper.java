package com.DannaS2.LogiTrack.mapper;

import com.DannaS2.LogiTrack.dto.request.MovimientoDtoRequest;
import com.DannaS2.LogiTrack.dto.response.DetalleMovimientoDtoResponse;
import com.DannaS2.LogiTrack.dto.response.MovimientoDtoResponse;
import com.DannaS2.LogiTrack.model.Bodega;
import com.DannaS2.LogiTrack.model.DetalleMovimiento;
import com.DannaS2.LogiTrack.model.Movimiento;
import com.DannaS2.LogiTrack.model.Persona;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovimientoMapper {

    private final DetalleMovimientoMapper detalleMapper;

    public MovimientoMapper(DetalleMovimientoMapper detalleMapper) {
        this.detalleMapper = detalleMapper;
    }

    public MovimientoDtoResponse entityToDto(Movimiento mov) {
        if (mov == null) return null;
        List<DetalleMovimientoDtoResponse> detalles = mov.getDetalles().stream()
                .map(detalleMapper::entityToDto)
                .toList();
        return new MovimientoDtoResponse(
                mov.getId(),
                mov.getFecha(),
                mov.getTipo(),
                mov.getPersona() != null ? mov.getPersona().getId() : null,
                mov.getPersona() != null
                        ? mov.getPersona().getNombre() + " " + mov.getPersona().getApellido()
                        : null,
                mov.getBodegaOrigen() != null ? mov.getBodegaOrigen().getId() : null,
                mov.getBodegaOrigen() != null ? mov.getBodegaOrigen().getNombre() : null,
                mov.getBodegaDestino() != null ? mov.getBodegaDestino().getId() : null,
                mov.getBodegaDestino() != null ? mov.getBodegaDestino().getNombre() : null,
                detalles
        );
    }

    // El service resuelve persona, bodegas y los detalles (con sus productos reales)
    public Movimiento DtoToEntity(MovimientoDtoRequest dto, Persona persona,
                                  Bodega origen, Bodega destino,
                                  List<DetalleMovimiento> detalles) {
        if (dto == null) return null;
        Movimiento m = new Movimiento();
        m.setTipo(dto.tipoMovimiento());
        m.setPersona(persona);
        m.setBodegaOrigen(origen);
        m.setBodegaDestino(destino);
        detalles.forEach(m::agregarDetalle);   // asigna el movimiento a cada detalle
        return m;
    }
}
