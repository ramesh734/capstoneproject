package com.wip.asset_management_system.service;


import java.util.List;

import com.wip.asset_management_system.dto.EmployeeDTO;

public interface EmployeeService {

    EmployeeDTO create(EmployeeDTO dto);

    EmployeeDTO getById(Long id);

    List<EmployeeDTO> getAll();

    EmployeeDTO update(Long id, EmployeeDTO dto);

    void delete(Long id);
}
