package com.wip.asset_management_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.wip.asset_management_system.dto.VendorDTO;
import com.wip.asset_management_system.service.VendorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vendors")
@Validated
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @PostMapping
    public ResponseEntity<VendorDTO> create(@Valid @RequestBody VendorDTO dto) {
        return ResponseEntity.ok(vendorService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendorDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(vendorService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<VendorDTO>> getAll() {
        return ResponseEntity.ok(vendorService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendorDTO> update(@PathVariable Long id,
                                            @Valid @RequestBody VendorDTO dto) {
        return ResponseEntity.ok(vendorService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        vendorService.delete(id);
        return ResponseEntity.ok("Vendor deleted successfully");
    }
}