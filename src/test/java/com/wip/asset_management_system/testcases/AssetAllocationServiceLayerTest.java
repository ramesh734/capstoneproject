package com.wip.asset_management_system.testcases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wip.asset_management_system.dto.AssetAllocationDTO;
import com.wip.asset_management_system.entity.Asset;
import com.wip.asset_management_system.entity.AssetAllocation;
import com.wip.asset_management_system.entity.Employee;
import com.wip.asset_management_system.exception.ResourceNotFoundException;
import com.wip.asset_management_system.repository.AssetAllocationRepository;
import com.wip.asset_management_system.repository.AssetRepository;
import com.wip.asset_management_system.repository.EmployeeRepository;
import com.wip.asset_management_system.serviceimpl.AssetAllocationServiceImpl;

@ExtendWith(MockitoExtension.class)
public class AssetAllocationServiceLayerTest {

    @Mock
    private AssetAllocationRepository allocationRepository;

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private AssetAllocationServiceImpl allocationService;

    @Test
    void testAllocateForcesStatusAssigned() {

        Asset asset = new Asset();
        asset.setAssetId(1L);
        asset.setAssetName("Laptop");

        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setEmployeeName("Ramesh");

        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        when(allocationRepository.save(any(AssetAllocation.class)))
                .thenAnswer(i -> i.getArgument(0));

        AssetAllocationDTO dto = new AssetAllocationDTO();
        dto.setAssetId(1L);
        dto.setEmployeeId(1L);
        dto.setAllocatedDate(LocalDate.now());
        dto.setStatus("Return");
        dto.setRemarks("Test");

        AssetAllocationDTO result = allocationService.allocate(dto);

        assertEquals("Assigned", result.getStatus());
    }

    @Test
    void testAllocateAssetNotFound() {

        when(assetRepository.findById(99L)).thenReturn(Optional.empty());

        AssetAllocationDTO dto = new AssetAllocationDTO();
        dto.setAssetId(99L);
        dto.setEmployeeId(1L);

        assertThrows(ResourceNotFoundException.class, () -> allocationService.allocate(dto));
    }

    @Test
    void testAllocateEmployeeNotFound() {

        Asset asset = new Asset();
        asset.setAssetId(1L);

        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        AssetAllocationDTO dto = new AssetAllocationDTO();
        dto.setAssetId(1L);
        dto.setEmployeeId(99L);

        assertThrows(ResourceNotFoundException.class, () -> allocationService.allocate(dto));
    }

    @Test
    void testGetAllocationById() {

        Asset asset = new Asset();
        asset.setAssetId(1L);
        asset.setAssetName("Laptop");

        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setEmployeeName("Ramesh");

        AssetAllocation allocation = new AssetAllocation();
        allocation.setAllocationId(1L);
        allocation.setAsset(asset);
        allocation.setEmployee(employee);
        allocation.setAllocatedDate(LocalDate.now());
        allocation.setStatus("Assigned");
        allocation.setRemarks("Office work");

        when(allocationRepository.findById(1L)).thenReturn(Optional.of(allocation));

        AssetAllocationDTO result = allocationService.getById(1L);

        assertEquals("Assigned", result.getStatus());
        assertEquals("Laptop", result.getAssetName());
        assertEquals("Ramesh", result.getEmployeeName());
    }

    @Test
    void testGetAllocationByIdNotFound() {

        when(allocationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> allocationService.getById(99L));
    }

    @Test
    void testGetAllAllocations() {

        Asset asset = new Asset();
        asset.setAssetId(1L);
        asset.setAssetName("Laptop");

        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setEmployeeName("Ramesh");

        AssetAllocation a1 = new AssetAllocation();
        a1.setAllocationId(1L);
        a1.setAsset(asset);
        a1.setEmployee(employee);
        a1.setStatus("Assigned");

        AssetAllocation a2 = new AssetAllocation();
        a2.setAllocationId(2L);
        a2.setAsset(asset);
        a2.setEmployee(employee);
        a2.setStatus("Return");

        when(allocationRepository.findAll()).thenReturn(List.of(a1, a2));

        List<AssetAllocationDTO> list = allocationService.getAll();

        assertEquals(2, list.size());
    }

    @Test
    void testUpdateAllowsAnyStatus() {

        Asset asset = new Asset();
        asset.setAssetId(1L);

        Employee employee = new Employee();
        employee.setEmployeeId(1L);

        AssetAllocation existing = new AssetAllocation();
        existing.setAllocationId(1L);
        existing.setAsset(asset);
        existing.setEmployee(employee);
        existing.setStatus("Assigned");

        when(allocationRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(allocationRepository.save(any(AssetAllocation.class)))
                .thenAnswer(i -> i.getArgument(0));

        AssetAllocationDTO dto = new AssetAllocationDTO();
        dto.setAssetId(1L);
        dto.setEmployeeId(1L);
        dto.setAllocatedDate(LocalDate.now());
        dto.setStatus("Return");
        dto.setRemarks("Returned");

        AssetAllocationDTO result = allocationService.update(1L, dto);

        assertEquals("Return", result.getStatus());
    }

    @Test
    void testDeleteAllocation() {

        AssetAllocation allocation = new AssetAllocation();
        allocation.setAllocationId(1L);

        when(allocationRepository.findById(1L)).thenReturn(Optional.of(allocation));

        assertDoesNotThrow(() -> allocationService.delete(1L));
        verify(allocationRepository).delete(allocation);
    }

    @Test
    void testDeleteAllocationNotFound() {

        when(allocationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> allocationService.delete(99L));
    }
}
