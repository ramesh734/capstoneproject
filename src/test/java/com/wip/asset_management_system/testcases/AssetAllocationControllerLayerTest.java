package com.wip.asset_management_system.testcases;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wip.asset_management_system.controller.AssetAllocationController;
import com.wip.asset_management_system.dto.AssetAllocationDTO;
import com.wip.asset_management_system.service.AssetAllocationService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AssetAllocationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AssetAllocationControllerLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AssetAllocationService allocationService;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Test
    void testAllocate() throws Exception {

        AssetAllocationDTO dto = new AssetAllocationDTO();
        dto.setAllocationId(1L);
        dto.setAssetId(1L);
        dto.setAssetName("Laptop");
        dto.setEmployeeId(1L);
        dto.setEmployeeName("Ramesh");
        dto.setAllocatedDate(LocalDate.now());
        dto.setStatus("Assigned");
        dto.setRemarks("Office work");

        when(allocationService.allocate(any(AssetAllocationDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/allocations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Assigned"))
                .andExpect(jsonPath("$.assetName").value("Laptop"));
    }

    @Test
    void testGetAllocationById() throws Exception {

        AssetAllocationDTO dto = new AssetAllocationDTO();
        dto.setAllocationId(1L);
        dto.setAssetId(1L);
        dto.setAssetName("Laptop");
        dto.setEmployeeId(1L);
        dto.setEmployeeName("Ramesh");
        dto.setStatus("Assigned");
        dto.setRemarks("Office work");

        when(allocationService.getById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/allocations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Assigned"));
    }

    @Test
    void testGetAllAllocations() throws Exception {

        AssetAllocationDTO dto1 = new AssetAllocationDTO();
        dto1.setAllocationId(1L);
        dto1.setAssetId(1L);
        dto1.setStatus("Assigned");

        AssetAllocationDTO dto2 = new AssetAllocationDTO();
        dto2.setAllocationId(2L);
        dto2.setAssetId(2L);
        dto2.setStatus("Return");

        when(allocationService.getAll()).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/api/allocations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void testUpdateAllocation() throws Exception {

        AssetAllocationDTO dto = new AssetAllocationDTO();
        dto.setAllocationId(1L);
        dto.setAssetId(1L);
        dto.setEmployeeId(1L);
        dto.setAllocatedDate(LocalDate.now());
        dto.setStatus("Return");
        dto.setRemarks("Returned device");

        when(allocationService.update(eq(1L), any(AssetAllocationDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/api/allocations/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Return"));
    }

    @Test
    void testDeleteAllocation() throws Exception {

        doNothing().when(allocationService).delete(1L);

        mockMvc.perform(delete("/api/allocations/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Allocation deleted successfully"));
    }
}
