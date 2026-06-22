package com.wip.asset_management_system.service;


import java.util.List;

import com.wip.asset_management_system.dto.AssetAllocationDTO;

public interface AssetAllocationService {

    AssetAllocationDTO allocate(AssetAllocationDTO dto);

    AssetAllocationDTO getById(Long id);

    List<AssetAllocationDTO> getAll();

    AssetAllocationDTO update(Long id, AssetAllocationDTO dto);

    void delete(Long id);
}
