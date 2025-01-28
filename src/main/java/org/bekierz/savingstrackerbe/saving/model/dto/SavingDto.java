package org.bekierz.savingstrackerbe.saving.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SavingDto(
        @JsonFormat(pattern = "0.00")
        @Positive
        Double amount,
        String assetCode,
        Double value,
        @Nullable
        Double exchangeRate
) {
}
