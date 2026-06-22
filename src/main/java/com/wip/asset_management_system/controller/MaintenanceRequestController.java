package com.wip.asset_management_system.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.wip.asset_management_system.converter.MaintenanceRequestConverter;
import com.wip.asset_management_system.dto.MaintenanceRequestDTO;
import com.wip.asset_management_system.entity.Asset;
import com.wip.asset_management_system.entity.Employee;
import com.wip.asset_management_system.entity.MaintenanceRequest;
import com.wip.asset_management_system.exception.ResourceNotFoundException;
import com.wip.asset_management_system.repository.AssetRepository;
import com.wip.asset_management_system.repository.EmployeeRepository;
import com.wip.asset_management_system.repository.MaintenanceRequestRepository;
import com.wip.asset_management_system.service.MaintenanceRequestService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/maintenance")
@Validated
public class MaintenanceRequestController {

    @Autowired
    private MaintenanceRequestService maintenanceService;

    @Autowired
    private MaintenanceRequestRepository maintenanceRepository;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private EmployeeRepository employeeRepo;

    // =========================
    // EMPLOYEE CREATE REQUEST
    // =========================
    @PostMapping
    public ResponseEntity<MaintenanceRequestDTO> create(
            @Valid @RequestBody MaintenanceRequestDTO dto,
            Principal principal) {

    	Employee emp = employeeRepo.findByUsername(principal.getName())
    	        .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        Asset asset = assetRepository.findById(dto.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));

        MaintenanceRequest m = new MaintenanceRequest();

        m.setAsset(asset);
        m.setDescription(dto.getDescription());

        // employee auto mapped from login
        m.setEmployeeId(emp.getEmployeeId());

        // default status for employee
        m.setStatus("OPEN");

        m.setCreatedAt(LocalDateTime.now());

        MaintenanceRequest saved = maintenanceRepository.save(m);

        return ResponseEntity.ok(
                MaintenanceRequestConverter.toDTO(saved)
        );
    }

    // =========================
    // GET BY ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceRequestDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(maintenanceService.getById(id));
    }

    // =========================
    // GET ALL (ADMIN VIEW)
    // =========================
    @GetMapping
    public ResponseEntity<List<MaintenanceRequestDTO>> getAll() {
        return ResponseEntity.ok(maintenanceService.getAll());
    }
    
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<MaintenanceRequestDTO>> getByEmployee(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                maintenanceService.getByEmployeeId(employeeId)
        );
    }
    // =========================
    // UPDATE (ADMIN ONLY)
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceRequestDTO> update(
            @PathVariable Long id,
            @RequestBody MaintenanceRequestDTO dto) {

        MaintenanceRequest m = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

        Asset asset = assetRepository.findById(dto.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));

        m.setAsset(asset);

        // admin controls status
        m.setStatus(dto.getStatus());

        MaintenanceRequest saved = maintenanceRepository.save(m);

        return ResponseEntity.ok(
                MaintenanceRequestConverter.toDTO(saved)
        );
    }

    // =========================
    // DELETE (ADMIN ONLY)
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        maintenanceService.delete(id);
        return ResponseEntity.ok("Maintenance request deleted successfully");
    }
}