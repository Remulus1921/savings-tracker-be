package org.bekierz.savingstrackerbe.datasource.model.api.response.crypto;

public record SingleCryptoResponse(
        Data data
) {
    public record Data(
            String id,
            String symbol,
            String name,
            String priceUsd
    ) {}
}
