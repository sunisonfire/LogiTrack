package com.DannaS2.LogiTrack.dto.response;

import java.util.List;

public record ReporteGeneralDtoResponse(
        List<StockBodega> stockPorBodega,
        List<ProductoMovido> productosMasMovidos
) {
    public record StockBodega(
            Long bodegaId,
            String bodegaNombre,
            Long stockTotal
    ) {
    }

    public record ProductoMovido(
            Long productoId,
            String productoNombre,
            Long cantidadMovida
    ) {
    }
}
