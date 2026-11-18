package com.DannaS2.LogiTrack.service.impl;

import com.DannaS2.LogiTrack.dto.response.ReporteGeneralDtoResponse;
import com.DannaS2.LogiTrack.repository.BodegaRepository;
import com.DannaS2.LogiTrack.repository.DetalleMovimientoRepository;
import com.DannaS2.LogiTrack.service.ReporteService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final BodegaRepository bodegaRepository;
    private final DetalleMovimientoRepository detalleMovimientoRepository;

    public ReporteServiceImpl(
            BodegaRepository bodegaRepository,
            DetalleMovimientoRepository detalleMovimientoRepository) {
        this.bodegaRepository = bodegaRepository;
        this.detalleMovimientoRepository = detalleMovimientoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ReporteGeneralDtoResponse obtenerResumenGeneral() {

        List<ReporteGeneralDtoResponse.StockBodega> stockPorBodega =
                bodegaRepository.obtenerStockTotalPorBodega()
                        .stream()
                        .map(fila -> new ReporteGeneralDtoResponse.StockBodega(
                                ((Number) fila[0]).longValue(),
                                (String) fila[1],
                                ((Number) fila[2]).longValue()
                        ))
                        .toList();

        List<ReporteGeneralDtoResponse.ProductoMovido> productosMasMovidos =
                detalleMovimientoRepository
                        .obtenerProductosMasMovidos(PageRequest.of(0, 5))
                        .stream()
                        .map(fila -> new ReporteGeneralDtoResponse.ProductoMovido(
                                ((Number) fila[0]).longValue(),
                                (String) fila[1],
                                ((Number) fila[2]).longValue()
                        ))
                        .toList();

        return new ReporteGeneralDtoResponse(
                stockPorBodega,
                productosMasMovidos
        );
    }
}