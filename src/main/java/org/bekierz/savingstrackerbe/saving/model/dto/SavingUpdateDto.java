package org.bekierz.savingstrackerbe.saving.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SavingUpdateDto(
        @NotNull
        @Positive
        Double amount
) {
}
