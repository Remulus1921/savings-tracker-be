package org.bekierz.savingstrackerbe.saving.controller;

import org.bekierz.savingstrackerbe.commons.controller.BaseController;
import org.bekierz.savingstrackerbe.commons.model.ApiResponse;
import org.bekierz.savingstrackerbe.saving.model.dto.SavingDto;
import org.bekierz.savingstrackerbe.saving.service.SavingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/saving")
public class SavingController extends BaseController {

    private final SavingService savingService;

    public SavingController(SavingService savingService) {
        this.savingService = savingService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SavingDto>>> getUserSavings() {
        List<SavingDto> savings = savingService.getUserSavings();

        return ok(savings, "Savings retrieved successfully");
    }

    @GetMapping("{assetCode}")
    public ResponseEntity<ApiResponse<Optional<SavingDto>>> getSavingValue(String assetCode) {
        Optional<SavingDto> saving = savingService.getSavingValue(assetCode);
        if (saving.isEmpty()) {
            return ok(saving, "Saving not found");
        }
        return ok(saving, "Saving retrieved successfully");
    }


}
