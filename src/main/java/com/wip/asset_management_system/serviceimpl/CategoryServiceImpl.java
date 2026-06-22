package com.wip.asset_management_system.serviceimpl;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wip.asset_management_system.converter.CategoryConverter;
import com.wip.asset_management_system.dto.CategoryDTO;
import com.wip.asset_management_system.entity.Category;
import com.wip.asset_management_system.exception.ResourceNotFoundException;
import com.wip.asset_management_system.repository.CategoryRepository;
import com.wip.asset_management_system.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public CategoryDTO create(CategoryDTO dto) {

        Category c = new Category();
        c.setCategoryName(dto.getCategoryName());

        return CategoryConverter.toDTO(categoryRepository.save(c));
    }

    @Override
    public CategoryDTO getById(Long id) {

        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        return CategoryConverter.toDTO(c);
    }

    @Override
    public List<CategoryDTO> getAll() {

        return categoryRepository.findAll()
                .stream()
                .map(CategoryConverter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO update(Long id, CategoryDTO dto) {

        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        c.setCategoryName(dto.getCategoryName());

        return CategoryConverter.toDTO(categoryRepository.save(c));
    }

    @Override
    public void delete(Long id) {

        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        categoryRepository.delete(c);
    }
}
