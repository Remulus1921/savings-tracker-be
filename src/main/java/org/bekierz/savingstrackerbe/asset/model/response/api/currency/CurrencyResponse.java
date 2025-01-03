package org.bekierz.savingstrackerbe.asset.model.response.api.currency;

import java.util.List;

public record CurrencyResponse(
        String table,
        String no,
        String effectiveDate,
        List<Rate> rates
) {
    public record Rate(
            String currency,
            String code,
            double ask
    ) {
    }
}
