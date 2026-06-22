package com.wip.asset_management_system.service;


import java.util.List;

import com.wip.asset_management_system.dto.VendorDTO;

public interface VendorService {

    VendorDTO create(VendorDTO dto);

    VendorDTO getById(Long id);

    List<VendorDTO> getAll();

    VendorDTO update(Long id, VendorDTO dto);

    void delete(Long id);
}
