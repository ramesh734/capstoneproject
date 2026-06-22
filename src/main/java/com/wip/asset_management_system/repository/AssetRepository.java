package com.wip.asset_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wip.asset_management_system.entity.Asset;

public interface AssetRepository extends JpaRepository<Asset, Long> {
}
