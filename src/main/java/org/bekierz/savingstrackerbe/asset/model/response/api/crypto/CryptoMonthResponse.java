package org.bekierz.savingstrackerbe.asset.model.response.api.crypto;

import java.util.List;

public record CryptoMonthResponse(
        List<Data> data
) {
    public record Data(
            String priceUsd,
            Long time
    ) {}
}
