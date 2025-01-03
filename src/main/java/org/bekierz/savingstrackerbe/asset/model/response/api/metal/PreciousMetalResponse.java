package org.bekierz.savingstrackerbe.asset.model.response.api.metal;


import java.util.List;
import java.util.Map;

public record PreciousMetalResponse(
    List<Map<String, Double>> rates
) {
}
