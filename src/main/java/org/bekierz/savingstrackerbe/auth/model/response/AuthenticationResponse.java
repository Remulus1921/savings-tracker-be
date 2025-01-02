package org.bekierz.savingstrackerbe.auth.model.response;

import lombok.Builder;

@Builder
public record AuthenticationResponse(String token) {
}
