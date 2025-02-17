package org.bekierz.savingstrackerbe.asset.controller;

import org.bekierz.savingstrackerbe.asset.model.dto.AssetTypeDto;
import org.bekierz.savingstrackerbe.asset.service.AssetTypeService;
import org.bekierz.savingstrackerbe.commons.controller.BaseController;
import org.bekierz.savingstrackerbe.commons.model.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/asset-types")
public class AssetTypeController extends BaseController {
    private final AssetTypeService assetTypeService;

    public AssetTypeController(AssetTypeService assetTypeService) {
        this.assetTypeService = assetTypeService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AssetTypeDto>>> getAssetTypes() {
        return ok(assetTypeService.getAssetTypes(), "Asset types retrieved successfully");
    }
}
