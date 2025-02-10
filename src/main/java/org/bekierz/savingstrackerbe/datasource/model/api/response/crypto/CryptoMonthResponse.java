package org.bekierz.savingstrackerbe.datasource.model.api.response.crypto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public record CryptoMonthResponse(
        List<Data> data,
        Long timestamp
) {
    public record Data(
            String priceUsd,
            Long time,
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            Date date
    ) {}
}
