package org.bekierz.savingstrackerbe.commons.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;

public record ApiResponse<T>(
        @Nullable
        @JsonInclude(JsonInclude.Include.NON_NULL)
        T data,
        String message,
        HttpStatus status
) {
}
