package com.wip.asset_management_system.serviceimpl;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wip.asset_management_system.converter.AssetConverter;
import com.wip.asset_management_system.dto.AssetDTO;
import com.wip.asset_management_system.entity.Asset;
import com.wip.asset_management_system.entity.Category;
import com.wip.asset_management_system.entity.Vendor;
import com.wip.asset_management_system.exception.ResourceNotFoundException;
import com.wip.asset_management_system.repository.AssetAllocationRepository;
import com.wip.asset_management_system.repository.AssetRepository;
import com.wip.asset_management_system.repository.CategoryRepository;
import com.wip.asset_management_system.repository.MaintenanceRequestRepository;
import com.wip.asset_management_system.repository.VendorRepository;
import com.wip.asset_management_system.service.AssetService;

@Service
public class AssetServiceImpl implements AssetService {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private AssetAllocationRepository allocationRepository;

    @Autowired
    private MaintenanceRequestRepository maintenanceRepository;

    @Override
    public AssetDTO create(AssetDTO dto) {

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Vendor vendor = vendorRepository.findById(dto.getVendorId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        Asset a = new Asset();
        a.setAssetName(dto.getAssetName());
        a.setSerialNumber(dto.getSerialNumber());
        a.setAvailability(dto.getAvailability());
        a.setCategory(category);
        a.setVendor(vendor);

        return AssetConverter.toDTO(assetRepository.save(a));
    }

    @Override
    public AssetDTO getById(Long id) {

        Asset a = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));

        return AssetConverter.toDTO(a);
    }

    @Override
    public List<AssetDTO> getAll() {

        return assetRepository.findAll()
                .stream()
                .map(AssetConverter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AssetDTO update(Long id, AssetDTO dto) {

        Asset a = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Vendor vendor = vendorRepository.findById(dto.getVendorId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        a.setAssetName(dto.getAssetName());
        a.setSerialNumber(dto.getSerialNumber());
        a.setAvailability(dto.getAvailability());
        a.setCategory(category);
        a.setVendor(vendor);

        return AssetConverter.toDTO(assetRepository.save(a));
    }

    @Override
    @Transactional
    public void delete(Long id) {

        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));

        // 🔥 IMPORTANT: delete dependent records first
        maintenanceRepository.deleteByAssetAssetId(id);
        allocationRepository.deleteByAssetAssetId(id);

        assetRepository.delete(asset);
    }
}
