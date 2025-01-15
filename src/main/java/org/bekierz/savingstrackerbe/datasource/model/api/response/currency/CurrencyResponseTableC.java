package org.bekierz.savingstrackerbe.datasource.model.api.response.currency;


import java.util.List;

public record CurrencyResponseTableC(
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
