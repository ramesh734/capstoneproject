package com.wip.asset_management_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.wip.asset_management_system.dto.AssetAllocationDTO;
import com.wip.asset_management_system.service.AssetAllocationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/allocations")
@Validated
public class AssetAllocationController {

    @Autowired
    private AssetAllocationService allocationService;

    @PostMapping
    public ResponseEntity<AssetAllocationDTO> allocate(@Valid @RequestBody AssetAllocationDTO dto) {
        return ResponseEntity.ok(allocationService.allocate(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetAllocationDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(allocationService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<AssetAllocationDTO>> getAll() {
        return ResponseEntity.ok(allocationService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssetAllocationDTO> update(@PathVariable Long id,
                                                    @Valid @RequestBody AssetAllocationDTO dto) {
        return ResponseEntity.ok(allocationService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        allocationService.delete(id);
        return ResponseEntity.ok("Allocation deleted successfully");
    }
}