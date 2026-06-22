package com.wip.asset_management_system.service;


import java.util.List;

import com.wip.asset_management_system.dto.CategoryDTO;

public interface CategoryService {

    CategoryDTO create(CategoryDTO dto);

    CategoryDTO getById(Long id);

    List<CategoryDTO> getAll();

    CategoryDTO update(Long id, CategoryDTO dto);

    void delete(Long id);
}
