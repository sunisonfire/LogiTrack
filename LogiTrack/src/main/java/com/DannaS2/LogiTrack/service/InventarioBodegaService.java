package com.DannaS2.LogiTrack.service;

import com.DannaS2.LogiTrack.dto.request.InventarioBodegaDtoRequest;
import com.DannaS2.LogiTrack.dto.response.InventarioBodegaDtoResponse;

import java.util.List;

public interface InventarioBodegaService {
    InventarioBodegaDtoResponse guardar(InventarioBodegaDtoRequest dto);
    InventarioBodegaDtoResponse actualizar(InventarioBodegaDtoRequest dto, Long id);
    void eliminar(Long id);
    List<InventarioBodegaDtoResponse> listar();
    InventarioBodegaDtoResponse buscarId(Long id);
    List<InventarioBodegaDtoResponse> buscarProducto(Long productoId);
    List<InventarioBodegaDtoResponse> buscarStockBajo(Integer limite);

    //EXAMEN
    //2.
    List<InventarioBodegaDtoResponse> buscarBodega(Long bodegaId);
}
