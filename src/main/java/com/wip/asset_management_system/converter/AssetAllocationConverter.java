package com.wip.asset_management_system.converter;

import com.wip.asset_management_system.dto.AssetAllocationDTO;
import com.wip.asset_management_system.entity.AssetAllocation;

public class AssetAllocationConverter {

    public static AssetAllocationDTO toDTO(AssetAllocation a) {

        AssetAllocationDTO dto = new AssetAllocationDTO();

        dto.setAllocationId(a.getAllocationId());

        dto.setAssetId(a.getAsset().getAssetId());
        dto.setAssetName(a.getAsset().getAssetName());

        dto.setEmployeeId(a.getEmployee().getEmployeeId());
        dto.setEmployeeName(a.getEmployee().getEmployeeName());

        dto.setAllocatedDate(a.getAllocatedDate());
        dto.setReturnDate(a.getReturnDate());

        dto.setStatus(a.getStatus());
        dto.setRemarks(a.getRemarks());

        return dto;
    }
}
