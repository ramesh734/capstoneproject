package com.wip.asset_management_system.converter;

import com.wip.asset_management_system.dto.MaintenanceRequestDTO;
import com.wip.asset_management_system.entity.MaintenanceRequest;

public class MaintenanceRequestConverter {

    public static MaintenanceRequestDTO toDTO(MaintenanceRequest m) {

        MaintenanceRequestDTO dto = new MaintenanceRequestDTO();

        dto.setRequestId(m.getRequestId());
        dto.setAssetId(m.getAsset().getAssetId());
        dto.setAssetName(m.getAsset().getAssetName());

        dto.setDescription(m.getDescription());
        dto.setStatus(m.getStatus());
        dto.setCreatedAt(m.getCreatedAt());
        dto.setEmployeeId(m.getEmployeeId());

        return dto;
    }
}
