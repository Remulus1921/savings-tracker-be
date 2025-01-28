package org.bekierz.savingstrackerbe.datasource.model.api.response.crypto;

import java.util.List;

public record CryptoMonthResponse(
        List<Data> data
) {
    public record Data(
            String priceUsd,
            Long time
    ) {}
}
