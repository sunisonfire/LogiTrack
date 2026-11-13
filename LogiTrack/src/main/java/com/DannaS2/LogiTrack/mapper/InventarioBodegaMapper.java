package com.DannaS2.LogiTrack.mapper;

import com.DannaS2.LogiTrack.dto.request.InventarioBodegaDtoRequest;
import com.DannaS2.LogiTrack.dto.response.InventarioBodegaDtoResponse;
import com.DannaS2.LogiTrack.model.Bodega;
import com.DannaS2.LogiTrack.model.InventarioBodega;
import com.DannaS2.LogiTrack.model.Producto;
import org.springframework.stereotype.Component;

@Component
public class InventarioBodegaMapper {

    public InventarioBodegaDtoResponse entityToDto(InventarioBodega inv) {
        if (inv == null) return null;
        return new InventarioBodegaDtoResponse(
                inv.getId(),
                inv.getBodega() != null ? inv.getBodega().getId() : null,
                inv.getBodega() != null ? inv.getBodega().getNombre() : null,
                inv.getProducto() != null ? inv.getProducto().getId() : null,
                inv.getProducto() != null ? inv.getProducto().getNombre() : null,
                inv.getCantidad()
        );
    }

    public InventarioBodega DtoToEntity(InventarioBodegaDtoRequest dto, Bodega bodega, Producto producto) {
        if (dto == null) return null;
        InventarioBodega inv = new InventarioBodega();
        inv.setBodega(bodega);
        inv.setProducto(producto);
        inv.setCantidad(dto.cantidad());
        return inv;
    }

    // Bodega y producto son la identidad del registro: solo se corrige la cantidad
    public void updateEntityToDto(InventarioBodega inv, InventarioBodegaDtoRequest dto) {
        inv.setCantidad(dto.cantidad());
    }
}
