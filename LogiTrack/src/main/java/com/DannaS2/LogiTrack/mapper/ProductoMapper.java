package com.DannaS2.LogiTrack.mapper;

import com.DannaS2.LogiTrack.dto.request.ProductoDtoRequest;
import com.DannaS2.LogiTrack.dto.response.ProductoDtoResponse;
import com.DannaS2.LogiTrack.model.Marca;
import com.DannaS2.LogiTrack.model.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public ProductoDtoResponse entityToDto(Producto producto) {
        if (producto == null) return null;
        return new ProductoDtoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getCategoria(),
                producto.getMarca() != null ? producto.getMarca().getId() : null,
                producto.getMarca() != null ? producto.getMarca().getNombre() : null
        );
    }

    // El request trae marcaId; el servicio debe buscar la Marca (marcaRepository.findById) y pasarla.
    public Producto DtoToEntity(ProductoDtoRequest dto, Marca marca) {
        if (dto == null) return null;
        Producto p = new Producto();
        p.setNombre(dto.nombre());
        p.setDescripcion(dto.descripcion());
        p.setPrecio(dto.precio());
        p.setCategoria(dto.categoria());
        p.setMarca(marca);
        return p;
    }

    public void updateEntityToDto(Producto producto, ProductoDtoRequest dto, Marca marca) {
        producto.setNombre(dto.nombre());
        producto.setDescripcion(dto.descripcion());
        producto.setPrecio(dto.precio());
        producto.setCategoria(dto.categoria());
        producto.setMarca(marca);
    }
}
