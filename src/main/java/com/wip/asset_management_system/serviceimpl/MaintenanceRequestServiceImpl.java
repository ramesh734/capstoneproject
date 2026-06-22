package com.wip.asset_management_system.serviceimpl;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

@Service
public class MaintenanceRequestServiceImpl implements MaintenanceRequestService {

    @Autowired
    private MaintenanceRequestRepository maintenanceRepository;

    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private EmployeeRepository employeeRepo;

    @Override
    public MaintenanceRequestDTO create(MaintenanceRequestDTO dto) {

        Asset asset = assetRepository.findById(dto.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));

        MaintenanceRequest m = new MaintenanceRequest();

        m.setAsset(asset);
        m.setDescription(dto.getDescription());
        m.setStatus("OPEN");
        m.setCreatedAt(LocalDateTime.now());
        m.setEmployeeId(dto.getEmployeeId());

        return MaintenanceRequestConverter.toDTO(maintenanceRepository.save(m));
    }

    @Override
    public MaintenanceRequestDTO getById(Long id) {

        MaintenanceRequest m = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

        return MaintenanceRequestConverter.toDTO(m);
    }

    @Override
    public List<MaintenanceRequestDTO> getAll() {

        return maintenanceRepository.findAll()
                .stream()
                .map(MaintenanceRequestConverter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MaintenanceRequestDTO update(Long id, MaintenanceRequestDTO dto) {

        MaintenanceRequest m = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

        Asset asset = assetRepository.findById(dto.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));

        m.setAsset(asset);
        m.setDescription(dto.getDescription());
        m.setStatus(dto.getStatus());

        return MaintenanceRequestConverter.toDTO(maintenanceRepository.save(m));
    }
    @Override
    public List<MaintenanceRequestDTO> getByEmployeeId(Long employeeId) {

        List<MaintenanceRequest> list =
                maintenanceRepository.findByEmployeeId(employeeId);

        return list.stream()
                .map(MaintenanceRequestConverter::toDTO)
                .toList();
    }

    @Override
    public void delete(Long id) {

        MaintenanceRequest m = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

        maintenanceRepository.delete(m);
    }
}