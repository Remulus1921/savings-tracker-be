package org.bekierz.savingstrackerbe.datasource.model.api.response.crypto;

import java.util.List;

public record CryptoResponse(
        List<Data> data
) {
    public record Data(
            String id,
            String symbol,
            String name,
            String priceUsd
    ) {}
}
