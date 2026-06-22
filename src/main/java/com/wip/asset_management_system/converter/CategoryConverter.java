package com.wip.asset_management_system.converter;

import com.wip.asset_management_system.dto.CategoryDTO;
import com.wip.asset_management_system.entity.Category;

public class CategoryConverter {

    public static CategoryDTO toDTO(Category c) {
        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryId(c.getCategoryId());
        dto.setCategoryName(c.getCategoryName());
        return dto;
    }
}
