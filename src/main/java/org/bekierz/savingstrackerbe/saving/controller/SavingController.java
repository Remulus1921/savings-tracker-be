package org.bekierz.savingstrackerbe.saving.controller;

import org.bekierz.savingstrackerbe.commons.controller.BaseController;
import org.bekierz.savingstrackerbe.commons.model.ApiResponse;
import org.bekierz.savingstrackerbe.saving.model.dto.SavingDto;
import org.bekierz.savingstrackerbe.saving.model.dto.SavingUpdateDto;
import org.bekierz.savingstrackerbe.saving.model.dto.SavingPostDto;
import org.bekierz.savingstrackerbe.saving.service.SavingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{assetCode}")
    public ResponseEntity<ApiResponse<Optional<SavingDto>>> getSavingValue(@PathVariable String assetCode) {
        Optional<SavingDto> saving = savingService.getSavingValue(assetCode);
        if (saving.isEmpty()) {
            return ok(saving, "Saving not found");
        }
        return ok(saving, "Saving retrieved successfully");
    }

    @GetMapping("/total")
    public ResponseEntity<ApiResponse<Double>> getTotalSavings() {
        Double total = savingService.getTotalSavings();
        return ok(total, "Total savings retrieved successfully");
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> postUserSaving(@RequestBody SavingPostDto saving) {
        savingService.addNewSaving(saving);
        return created("Saving added successfully");
    }

    @PutMapping("/{assetCode}")
    public ResponseEntity<ApiResponse<SavingDto>> updateUserSaving(@PathVariable String assetCode, @RequestBody SavingUpdateDto updateDto) {
        SavingDto result = savingService.updateSaving(updateDto, assetCode);
        return ok(result, "Saving updated successfully");
    }

    @DeleteMapping("/{assetCode}")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "No content, resource deleted")
    public ResponseEntity<ApiResponse<Void>> deleteSaving(@PathVariable String assetCode) {
        savingService.deleteSaving(assetCode);
        return noContent("Saving deleted successfully");
    }
}
