package com.cheko.backend.service;

import com.cheko.backend.dto.ItemDto;
import com.cheko.backend.model.Category;
import com.cheko.backend.model.Item;
import com.cheko.backend.repository.CategoryRepository;
import com.cheko.backend.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Get all items with pagination
    @Transactional(readOnly = true)
    public Page<ItemDto> getAllItems(Pageable pageable) {
        return itemRepository.findAllItems(pageable)
                .map(this::convertToDto);
    }

    // Get item by ID
    @Transactional(readOnly = true)
    public Optional<ItemDto> getItemById(Long id) {
        return itemRepository.findById(id)
                .map(this::convertToDto);
    }

    // Search items by name OR description (main search requirement)
    @Transactional(readOnly = true)
    public Page<ItemDto> searchItems(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return getAllItems(pageable);
        }
        return itemRepository.searchByNameOrDescription(query.trim(), pageable)
                .map(this::convertToDto);
    }

    // Filter items by dish type/category (main filter requirement)
    @Transactional(readOnly = true)
    public Page<ItemDto> filterByDishType(String dishType, Pageable pageable) {
        return itemRepository.findByCategoryName(dishType, pageable)
                .map(this::convertToDto);
    }

    // Combined search and filter (main combined requirement)
    @Transactional(readOnly = true)
    public Page<ItemDto> searchAndFilter(String query, String dishType, Boolean bestSeller, 
                                        Boolean available, Pageable pageable) {
        return itemRepository.searchAndFilter(query, dishType, bestSeller, available, pageable)
                .map(this::convertToDto);
    }

    // Get best seller items
    @Transactional(readOnly = true)
    public List<ItemDto> getBestSellerItems() {
        return itemRepository.findBestSellers()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Get item counts per category (for category cards display)
    @Transactional(readOnly = true)
    public Map<String, Long> getItemCountsByCategory() {
        List<Object[]> results = itemRepository.getCategoryItemCounts();
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

    // Increment order count when item is ordered
    public void incrementOrderCount(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));
        
        item.incrementOrderCount();
        itemRepository.save(item);
    }

    // Create new item
    public ItemDto createItem(ItemDto itemDto) {
        // Check if item already exists
        if (itemRepository.existsByNameIgnoreCase(itemDto.getName())) {
            throw new RuntimeException("Item with name '" + itemDto.getName() + "' already exists");
        }

        // Get category
        Category category = categoryRepository.findById(itemDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + itemDto.getCategoryId()));

        Item item = convertToEntity(itemDto, category);
        Item savedItem = itemRepository.save(item);
        return convertToDto(savedItem);
    }

    // Update item
    public ItemDto updateItem(Long id, ItemDto itemDto) {
        Item existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

        // Check if new name conflicts with existing item (excluding current one)
        if (!existingItem.getName().equalsIgnoreCase(itemDto.getName()) &&
            itemRepository.existsByNameIgnoreCase(itemDto.getName())) {
            throw new RuntimeException("Item with name '" + itemDto.getName() + "' already exists");
        }

        // Get category if changed
        if (!existingItem.getCategory().getId().equals(itemDto.getCategoryId())) {
            Category category = categoryRepository.findById(itemDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + itemDto.getCategoryId()));
            existingItem.setCategory(category);
        }

        // Update fields
        existingItem.setName(itemDto.getName());
        existingItem.setDescription(itemDto.getDescription());
        existingItem.setPrice(itemDto.getPrice());
        existingItem.setCalories(itemDto.getCalories());
        existingItem.setImageUrl(itemDto.getImageUrl());
        existingItem.setIsAvailable(itemDto.getIsAvailable());

        Item updatedItem = itemRepository.save(existingItem);
        return convertToDto(updatedItem);
    }

    // Soft delete item
    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
        itemRepository.delete(item); // This will trigger soft delete due to @SQLDelete annotation
    }

    // Update best sellers based on total orders (scheduled task)
    @Scheduled(fixedRate = 3600000) // Run every hour
    public void updateBestSellers() {
        // Reset all best sellers
        itemRepository.resetAllBestSellers();
        
        // Get top 5 most ordered items
        List<Item> topItems = itemRepository.findTopItemsByOrders(PageRequest.of(0, 5));
        
        if (!topItems.isEmpty()) {
            List<Long> topItemIds = topItems.stream()
                    .map(Item::getId)
                    .collect(Collectors.toList());
            
            // Mark them as best sellers
            itemRepository.setBestSellers(topItemIds);
        }
    }

    // Manually refresh best sellers
    public void refreshBestSellers() {
        updateBestSellers();
    }

    // Filter by availability
    @Transactional(readOnly = true)
    public Page<ItemDto> filterByAvailability(Boolean available, Pageable pageable) {
        return itemRepository.findByAvailability(available, pageable)
                .map(this::convertToDto);
    }

    // Filter by calorie range
    @Transactional(readOnly = true)
    public Page<ItemDto> filterByCalorieRange(Integer minCalories, Integer maxCalories, Pageable pageable) {
        return itemRepository.findByCalorieRange(minCalories, maxCalories, pageable)
                .map(this::convertToDto);
    }

    // Convert Entity to DTO
    private ItemDto convertToDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getCalories(),
                item.getImageUrl(),
                item.getCategory().getId(),
                item.getCategory().getName(),
                item.getIsAvailable(),
                item.getTotalOrders(),
                item.getIsBestSeller()
        );
    }

    // Convert DTO to Entity
    private Item convertToEntity(ItemDto itemDto, Category category) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setPrice(itemDto.getPrice());
        item.setCalories(itemDto.getCalories());
        item.setImageUrl(itemDto.getImageUrl());
        item.setCategory(category);
        item.setIsAvailable(itemDto.getIsAvailable());
        return item;
    }
}

