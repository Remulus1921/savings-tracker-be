package org.bekierz.savingstrackerbe.datasource.model.api.response.metal;


import java.util.List;
import java.util.Map;

public record PreciousMetalResponse(
    List<Map<String, Double>> rates
) {
}
