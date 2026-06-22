package com.wip.asset_management_system.service;


import java.util.List;

import com.wip.asset_management_system.dto.MaintenanceRequestDTO;

public interface MaintenanceRequestService {

    MaintenanceRequestDTO create(MaintenanceRequestDTO dto);

    MaintenanceRequestDTO getById(Long id);

    List<MaintenanceRequestDTO> getAll();

    MaintenanceRequestDTO update(Long id, MaintenanceRequestDTO dto);
    List<MaintenanceRequestDTO> getByEmployeeId(Long employeeId);

    void delete(Long id);
}
