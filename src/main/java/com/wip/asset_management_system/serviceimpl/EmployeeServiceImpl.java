package com.wip.asset_management_system.serviceimpl;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wip.asset_management_system.converter.EmployeeConverter;
import com.wip.asset_management_system.dto.EmployeeDTO;
import com.wip.asset_management_system.entity.Employee;
import com.wip.asset_management_system.exception.DuplicateResourceException;
import com.wip.asset_management_system.exception.ResourceNotFoundException;
import com.wip.asset_management_system.repository.AssetAllocationRepository;
import com.wip.asset_management_system.repository.EmployeeRepository;
import com.wip.asset_management_system.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	private PasswordEncoder passwordEncoder;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AssetAllocationRepository allocationRepository;

    @Override
    public EmployeeDTO create(EmployeeDTO dto) {

        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        if (employeeRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }

        Employee emp = new Employee();

        emp.setEmployeeName(dto.getEmployeeName());
        emp.setEmail(dto.getEmail());
        emp.setDepartment(dto.getDepartment());
        emp.setUsername(dto.getUsername());
        emp.setPassword(passwordEncoder.encode(dto.getPassword()));
        emp.setRole(dto.getRole());

        return EmployeeConverter.toDTO(employeeRepository.save(emp));
    }

    @Override
    public EmployeeDTO getById(Long id) {

        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        return EmployeeConverter.toDTO(emp);
    }

    @Override
    public List<EmployeeDTO> getAll() {

        return employeeRepository.findAll()
                .stream()
                .map(EmployeeConverter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO update(Long id, EmployeeDTO dto) {

        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        emp.setEmployeeName(dto.getEmployeeName());
        emp.setEmail(dto.getEmail());
        emp.setDepartment(dto.getDepartment());
        emp.setUsername(dto.getUsername());
        if(dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            emp.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        emp.setRole(dto.getRole());

        return EmployeeConverter.toDTO(employeeRepository.save(emp));
    }

    @Override
    @Transactional
    public void delete(Long id) {

        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        allocationRepository.deleteByEmployeeEmployeeId(id);
        allocationRepository.flush();

        employeeRepository.delete(emp);
    }
}
