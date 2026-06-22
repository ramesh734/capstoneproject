package com.wip.asset_management_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.wip.asset_management_system.dto.AssetDTO;
import com.wip.asset_management_system.service.AssetService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/assets")
@Validated
public class AssetController {

    @Autowired
    private AssetService assetService;

    @PostMapping
    public ResponseEntity<AssetDTO> create(@Valid @RequestBody AssetDTO dto) {
        return ResponseEntity.ok(assetService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(assetService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<AssetDTO>> getAll() {
        return ResponseEntity.ok(assetService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssetDTO> update(@PathVariable Long id,
                                          @Valid @RequestBody AssetDTO dto) {
        return ResponseEntity.ok(assetService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        assetService.delete(id);
        return ResponseEntity.ok("Asset deleted successfully");
    }
}