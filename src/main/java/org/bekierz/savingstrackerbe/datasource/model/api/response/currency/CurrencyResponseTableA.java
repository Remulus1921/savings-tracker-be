package org.bekierz.savingstrackerbe.datasource.model.api.response.currency;

import java.util.List;

public record CurrencyResponseTableA(
        String table,
        String no,
        String effectiveDate,
        List<Rate> rates
) {
    public record Rate(
            String currency,
            String code,
            double mid
    ) {
    }
}
