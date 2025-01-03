package org.bekierz.savingstrackerbe.asset.model.response.api.crypto;

import java.util.List;

public record CryptoResponse(
        List<Data> data
) {
    private record Data(
            String id,
            String symbol,
            String name
    ) {}
}
