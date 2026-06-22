package com.wip.asset_management_system.testcases;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.asset_management_system.controller.EmployeeController;
import com.wip.asset_management_system.dto.EmployeeDTO;
import com.wip.asset_management_system.service.EmployeeService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ControllerLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetEmployeeById() throws Exception {

        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeId(1L);
        dto.setEmployeeName("Ramesh");
        dto.setEmail("ramesh@gmail.com");
        dto.setDepartment("IT");
        dto.setUsername("ramesh");
        dto.setPassword("ramesh123");
        dto.setRole("ROLE_EMPLOYEE");

        when(employeeService.getById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeName").value("Ramesh"));
    }

    @Test
    void testGetAllEmployees() throws Exception {

        EmployeeDTO dto1 = new EmployeeDTO();
        dto1.setEmployeeId(1L);
        dto1.setEmployeeName("Ramesh");
        dto1.setEmail("ramesh@gmail.com");
        dto1.setDepartment("IT");
        dto1.setUsername("ramesh");
        dto1.setPassword("ramesh123");
        dto1.setRole("ROLE_EMPLOYEE");

        EmployeeDTO dto2 = new EmployeeDTO();
        dto2.setEmployeeId(2L);
        dto2.setEmployeeName("Suresh");
        dto2.setEmail("suresh@gmail.com");
        dto2.setDepartment("HR");
        dto2.setUsername("suresh");
        dto2.setPassword("suresh123");
        dto2.setRole("ROLE_EMPLOYEE");

        when(employeeService.getAll()).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void testCreateEmployee() throws Exception {

        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeName("John");
        dto.setEmail("john@gmail.com");
        dto.setDepartment("IT");
        dto.setUsername("john");
        dto.setPassword("john123");
        dto.setRole("ROLE_EMPLOYEE");

        when(employeeService.create(any(EmployeeDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeName").value("John"));
    }

    @Test
    void testUpdateEmployee() throws Exception {

        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeName("Updated");
        dto.setEmail("updated@gmail.com");
        dto.setDepartment("IT");
        dto.setUsername("updated");
        dto.setPassword("pass123");
        dto.setRole("ROLE_EMPLOYEE");

        when(employeeService.update(eq(1L), any(EmployeeDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/api/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeName").value("Updated"));
    }

    @Test
    void testDeleteEmployee() throws Exception {

        doNothing().when(employeeService).delete(1L);

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isOk());
    }
}