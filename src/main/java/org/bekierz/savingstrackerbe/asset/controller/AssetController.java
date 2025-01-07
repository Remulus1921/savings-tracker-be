package org.bekierz.savingstrackerbe.asset.controller;

import org.bekierz.savingstrackerbe.asset.model.dto.AssetDto;
import org.bekierz.savingstrackerbe.asset.model.dto.AssetMonthValueDto;
import org.bekierz.savingstrackerbe.asset.service.AssetService;
import org.bekierz.savingstrackerbe.commons.controller.BaseController;
import org.bekierz.savingstrackerbe.commons.model.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/assets")
public class AssetController extends BaseController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping("/{assetType}")
    public ResponseEntity<ApiResponse<List<AssetDto>>> getAssets(@PathVariable String assetType) {
        List<AssetDto> assets = assetService.getAssets(assetType);
        return ok(assets, "Assets retrieved successfully");
    }

    @GetMapping("/month/{assetCode}")
    public ResponseEntity<ApiResponse<List<AssetMonthValueDto>>> getMonthValue(@PathVariable String assetCode) {
        List<AssetMonthValueDto> assets = assetService.getMonthValue(assetCode);
        return ok(assets, "Asset retrieved successfully");
    }
}
