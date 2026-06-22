package com.wip.asset_management_system.testcases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wip.asset_management_system.dto.EmployeeDTO;
import com.wip.asset_management_system.entity.Employee;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.wip.asset_management_system.exception.DuplicateResourceException;
import com.wip.asset_management_system.exception.ResourceNotFoundException;
import com.wip.asset_management_system.repository.AssetAllocationRepository;
import com.wip.asset_management_system.repository.EmployeeRepository;
import com.wip.asset_management_system.serviceimpl.EmployeeServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ServiceLayerTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AssetAllocationRepository allocationRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void testGetEmployeeById() {

        Employee emp = new Employee();
        emp.setEmployeeId(1L);
        emp.setEmployeeName("Ramesh");

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(emp));

        EmployeeDTO dto = employeeService.getById(1L);

        assertEquals("Ramesh", dto.getEmployeeName());
    }

    @Test
    void testGetAllEmployees() {

        Employee emp1 = new Employee();
        emp1.setEmployeeId(1L);
        emp1.setEmployeeName("Ramesh");

        Employee emp2 = new Employee();
        emp2.setEmployeeId(2L);
        emp2.setEmployeeName("Suresh");

        when(employeeRepository.findAll()).thenReturn(List.of(emp1, emp2));

        List<EmployeeDTO> list = employeeService.getAll();

        assertEquals(2, list.size());
        assertEquals("Ramesh", list.get(0).getEmployeeName());
        assertEquals("Suresh", list.get(1).getEmployeeName());
    }

    @Test
    void testUpdateEmployee() {

        Employee emp = new Employee();
        emp.setEmployeeId(1L);
        emp.setEmployeeName("Old Name");

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(emp));

        when(employeeRepository.save(any(Employee.class))).thenAnswer(i -> i.getArgument(0));

        when(passwordEncoder.encode(anyString())).thenReturn("encoded");

        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeName("New Name");
        dto.setEmail("new@email.com");
        dto.setDepartment("IT");
        dto.setUsername("newuser");
        dto.setPassword("pass123");
        dto.setRole("ROLE_EMPLOYEE");

        EmployeeDTO result = employeeService.update(1L, dto);

        assertEquals("New Name", result.getEmployeeName());
    }

    @Test
    void testCreateEmployeeDuplicateEmail() {

        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeName("Test");
        dto.setEmail("dup@email.com");
        dto.setUsername("testuser");
        dto.setPassword("pass123");
        dto.setDepartment("IT");
        dto.setRole("ROLE_EMPLOYEE");

        when(employeeRepository.existsByEmail("dup@email.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> employeeService.create(dto));
    }

    @Test
    void testDeleteEmployee() {

        Employee emp = new Employee();
        emp.setEmployeeId(1L);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(emp));

        doNothing().when(allocationRepository).deleteByEmployeeEmployeeId(1L);

        assertDoesNotThrow(() -> employeeService.delete(1L));

        verify(allocationRepository).deleteByEmployeeEmployeeId(1L);
        verify(employeeRepository).delete(emp);
    }

    @Test
    void testDeleteEmployeeNotFound() {

        when(employeeRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.delete(99L));
    }
}