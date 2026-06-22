package com.wip.asset_management_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wip.asset_management_system.entity.AssetAllocation;

public interface AssetAllocationRepository extends JpaRepository<AssetAllocation, Long> {

    List<AssetAllocation> findByAssetAssetId(Long assetId);

    @Modifying
    @Query("DELETE FROM AssetAllocation a WHERE a.asset.assetId = :assetId")
    void deleteByAssetAssetId(@Param("assetId") Long assetId);

    List<AssetAllocation> findByEmployeeEmployeeId(Long employeeId);

    @Modifying
    @Query("DELETE FROM AssetAllocation a WHERE a.employee.employeeId = :employeeId")
    void deleteByEmployeeEmployeeId(@Param("employeeId") Long employeeId);
}