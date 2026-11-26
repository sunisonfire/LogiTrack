package com.DannaS2.LogiTrack.service;

import com.DannaS2.LogiTrack.dto.request.ProductoDtoRequest;
import com.DannaS2.LogiTrack.dto.response.BodegaDtoResponse;
import com.DannaS2.LogiTrack.dto.response.ProductoDtoResponse;
import com.DannaS2.LogiTrack.model.Categoria;

import java.util.List;

public interface ProductoService {
    ProductoDtoResponse guardar(ProductoDtoRequest dto);
    ProductoDtoResponse actualizar(ProductoDtoRequest dto, Long id);
    void eliminar(Long id);
    List<ProductoDtoResponse> listar();
    ProductoDtoResponse buscarId(Long id);
    List<ProductoDtoResponse> buscarCategoria(Categoria categoria);

    //EXAMEN
    //3.
    List<ProductoDtoResponse> buscarNombre(String texto);


}
