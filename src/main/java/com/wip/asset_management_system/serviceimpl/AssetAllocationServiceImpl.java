package com.wip.asset_management_system.serviceimpl;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wip.asset_management_system.converter.AssetAllocationConverter;
import com.wip.asset_management_system.dto.AssetAllocationDTO;
import com.wip.asset_management_system.entity.Asset;
import com.wip.asset_management_system.entity.AssetAllocation;
import com.wip.asset_management_system.entity.Employee;
import com.wip.asset_management_system.exception.ResourceNotFoundException;
import com.wip.asset_management_system.repository.AssetAllocationRepository;
import com.wip.asset_management_system.repository.AssetRepository;
import com.wip.asset_management_system.repository.EmployeeRepository;
import com.wip.asset_management_system.service.AssetAllocationService;

@Service
public class AssetAllocationServiceImpl implements AssetAllocationService {

    @Autowired
    private AssetAllocationRepository allocationRepository;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public AssetAllocationDTO allocate(AssetAllocationDTO dto) {

        Asset asset = assetRepository.findById(dto.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        AssetAllocation allocation = new AssetAllocation();

        allocation.setAsset(asset);
        allocation.setEmployee(employee);
        allocation.setAllocatedDate(dto.getAllocatedDate());
        allocation.setReturnDate(dto.getReturnDate());
        allocation.setStatus("Assigned");
        allocation.setRemarks(dto.getRemarks());

        return AssetAllocationConverter.toDTO(allocationRepository.save(allocation));
    }

    @Override
    public AssetAllocationDTO getById(Long id) {

        AssetAllocation a = allocationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Allocation not found"));

        return AssetAllocationConverter.toDTO(a);
    }

    @Override
    public List<AssetAllocationDTO> getAll() {

        return allocationRepository.findAll()
                .stream()
                .map(AssetAllocationConverter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AssetAllocationDTO update(Long id, AssetAllocationDTO dto) {

        AssetAllocation a = allocationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Allocation not found"));

        Asset asset = assetRepository.findById(dto.getAssetId())
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        a.setAsset(asset);
        a.setEmployee(employee);
        a.setAllocatedDate(dto.getAllocatedDate());
        a.setReturnDate(dto.getReturnDate());
        a.setStatus(dto.getStatus());
        a.setRemarks(dto.getRemarks());

        return AssetAllocationConverter.toDTO(allocationRepository.save(a));
    }

    @Override
    public void delete(Long id) {

        AssetAllocation a = allocationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Allocation not found"));

        allocationRepository.delete(a);
    }
}
