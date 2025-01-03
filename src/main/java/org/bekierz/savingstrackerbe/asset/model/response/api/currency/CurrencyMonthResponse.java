package org.bekierz.savingstrackerbe.asset.model.response.api.currency;


import java.util.List;

public record CurrencyMonthResponse(
        String table,
        String currency,
        String code,
        List<Rate> rates
) {
    public record Rate(
            String no,
            String effectiveDate,
            double bid,
            double ask
    ) {
    }
}
