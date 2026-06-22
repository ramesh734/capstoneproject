package com.wip.asset_management_system.serviceimpl;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wip.asset_management_system.converter.VendorConverter;
import com.wip.asset_management_system.dto.VendorDTO;
import com.wip.asset_management_system.entity.Vendor;
import com.wip.asset_management_system.exception.ResourceNotFoundException;
import com.wip.asset_management_system.repository.VendorRepository;
import com.wip.asset_management_system.service.VendorService;

@Service
public class VendorServiceImpl implements VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    @Override
    public VendorDTO create(VendorDTO dto) {

        Vendor v = new Vendor();
        v.setVendorName(dto.getVendorName());
        v.setContactNumber(dto.getContactNumber());
        v.setEmail(dto.getEmail());
        v.setAddress(dto.getAddress());

        return VendorConverter.toDTO(vendorRepository.save(v));
    }

    @Override
    public VendorDTO getById(Long id) {

        Vendor v = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        return VendorConverter.toDTO(v);
    }

    @Override
    public List<VendorDTO> getAll() {

        return vendorRepository.findAll()
                .stream()
                .map(VendorConverter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VendorDTO update(Long id, VendorDTO dto) {

        Vendor v = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        v.setVendorName(dto.getVendorName());
        v.setContactNumber(dto.getContactNumber());
        v.setEmail(dto.getEmail());
        v.setAddress(dto.getAddress());

        return VendorConverter.toDTO(vendorRepository.save(v));
    }

    @Override
    public void delete(Long id) {

        Vendor v = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));

        vendorRepository.delete(v);
    }
}
