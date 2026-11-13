package com.DannaS2.LogiTrack.mapper;

import com.DannaS2.LogiTrack.dto.request.MovimientoDtoRequest;
import com.DannaS2.LogiTrack.dto.response.DetalleMovimientoDtoResponse;
import com.DannaS2.LogiTrack.dto.response.MovimientoDtoResponse;
import com.DannaS2.LogiTrack.model.DetalleMovimiento;
import com.DannaS2.LogiTrack.model.Movimiento;
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

    public Movimiento DtoToEntity(MovimientoDtoRequest dto) {
        if (dto == null) return null;
        Movimiento m = new Movimiento();
        m.setTipo(dto.tipoMovimiento());
        m.setPersona(dto.persona());
        m.setBodegaOrigen(dto.bodegaOrigen());
        m.setBodegaDestino(dto.bodegaDestino());
        if (dto.detalles() != null) {
            for (DetalleMovimiento d : dto.detalles()) {
                m.agregarDetalle(d); // asigna el movimiento al detalle y lo agrega a la lista
            }
        }
        return m;
    }

    public void updateEntityToDto(Movimiento mov, MovimientoDtoRequest dto) {
        mov.setTipo(dto.tipoMovimiento());
        mov.setPersona(dto.persona());
        mov.setBodegaOrigen(dto.bodegaOrigen());
        mov.setBodegaDestino(dto.bodegaDestino());
        mov.getDetalles().clear(); // orphanRemoval elimina los detalles anteriores
        if (dto.detalles() != null) {
            for (DetalleMovimiento d : dto.detalles()) {
                mov.agregarDetalle(d);
            }
        }
    }
}
