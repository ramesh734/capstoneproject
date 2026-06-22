package com.wip.asset_management_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wip.asset_management_system.entity.Employee;
import com.wip.asset_management_system.entity.MaintenanceRequest;

public interface MaintenanceRequestRepository extends JpaRepository<MaintenanceRequest, Long> {

    List<MaintenanceRequest> findByAssetAssetId(Long assetId);
    List<MaintenanceRequest> findByEmployeeId(Long employeeId);
    

    void deleteByAssetAssetId(Long assetId);
}