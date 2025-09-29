package com.cheko.backend.service;

import com.cheko.backend.dto.CategoryDto;
import com.cheko.backend.model.Category;
import com.cheko.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Get all categories ordered by name
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAllOrderedByName()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Get category by ID
    @Transactional(readOnly = true)
    public Optional<CategoryDto> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(this::convertToDto);
    }

    // Get category by name
    @Transactional(readOnly = true)
    public Optional<CategoryDto> getCategoryByName(String name) {
        return categoryRepository.findByNameIgnoreCase(name)
                .map(this::convertToDto);
    }

    // Get item counts per category (for category cards display)
    @Transactional(readOnly = true)
    public Map<String, Long> getItemCountsByCategory() {
        List<Object[]> results = categoryRepository.getCategoryItemCounts();
        Map<String, Long> counts = new HashMap<>();
        long total = 0;

        for (Object[] result : results) {
            String categoryName = (String) result[0];
            Long count = ((Number) result[1]).longValue();
            counts.put(categoryName, count);
            total += count;
        }
        counts.put("total", total);
        return counts;
    }

    // Create new category
    public CategoryDto createCategory(CategoryDto categoryDto) {
        // Check if category already exists
        if (categoryRepository.existsByNameIgnoreCase(categoryDto.getName())) {
            throw new RuntimeException("Category with name '" + categoryDto.getName() + "' already exists");
        }

        Category category = convertToEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return convertToDto(savedCategory);
    }

    // Update category
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        // Check if new name conflicts with existing category (excluding current one)
        if (!existingCategory.getName().equalsIgnoreCase(categoryDto.getName()) &&
            categoryRepository.existsByNameIgnoreCase(categoryDto.getName())) {
            throw new RuntimeException("Category with name '" + categoryDto.getName() + "' already exists");
        }

        existingCategory.setName(categoryDto.getName());
        existingCategory.setDescription(categoryDto.getDescription());
        existingCategory.setIconName(categoryDto.getIconName());

        Category updatedCategory = categoryRepository.save(existingCategory);
        return convertToDto(updatedCategory);
    }

    // Soft delete category
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        categoryRepository.delete(category); // This will trigger soft delete due to @SQLDelete annotation
    }

    // Check if category exists
    @Transactional(readOnly = true)
    public boolean categoryExists(String name) {
        return categoryRepository.existsByNameIgnoreCase(name);
    }

    // Get categories with at least N items (for validation)
    @Transactional(readOnly = true)
    public List<String> getCategoriesWithAtLeastItems(int minItems) {
        return categoryRepository.findCategoriesWithAtLeastItems(minItems);
    }

    // Convert Entity to DTO
    private CategoryDto convertToDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getIconName(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

    // Convert DTO to Entity
    private Category convertToEntity(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category.setIconName(categoryDto.getIconName());
        return category;
    }
}

