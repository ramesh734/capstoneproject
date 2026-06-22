package com.wip.asset_management_system.service;


import java.util.List;

import com.wip.asset_management_system.dto.AssetDTO;

public interface AssetService {

    AssetDTO create(AssetDTO dto);

    AssetDTO getById(Long id);

    List<AssetDTO> getAll();

    AssetDTO update(Long id, AssetDTO dto);

    void delete(Long id);
}
