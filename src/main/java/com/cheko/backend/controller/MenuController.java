package com.cheko.backend.controller;

import com.cheko.backend.dto.CategoryDto;
import com.cheko.backend.dto.ItemDto;
import com.cheko.backend.dto.SecondHighestCalorieDto;
import com.cheko.backend.service.CalorieAnalysisService;
import com.cheko.backend.service.CategoryService;
import com.cheko.backend.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/menu")
@CrossOrigin(origins = "*") // Allow all origins for development
@Tag(name = "Menu Management", description = "APIs for managing menu items, search, filter, and calorie analysis")
public class MenuController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CalorieAnalysisService calorieAnalysisService;

    // Get all menu items (paginated)
    @Operation(summary = "Get all menu items", description = "Retrieve all menu items with pagination support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved menu items")
    })
    @GetMapping("/items")
    public ResponseEntity<Page<ItemDto>> getAllItems(
            @Parameter(description = "Pagination parameters") @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(itemService.getAllItems(pageable));
    }

    // Get item details by ID (for popup)
    @GetMapping("/items/{id}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable Long id) {
        return itemService.getItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Search items by name OR description (main search requirement)
    @Operation(summary = "Search menu items", 
               description = "🔍 MAIN SEARCH REQUIREMENT: Search menu items by name OR description containing the query string (e.g., 'soup')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved search results")
    })
    @GetMapping("/items/search")
    public ResponseEntity<Page<ItemDto>> searchItems(
            @Parameter(description = "Search query - searches in item name OR description", example = "soup") 
            @RequestParam(required = false) String q,
            @Parameter(description = "Pagination parameters") @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(itemService.searchItems(q, pageable));
    }

    // Filter items by dish type (main filter requirement)
    @GetMapping("/items/filter")
    public ResponseEntity<Page<ItemDto>> filterItems(
            @RequestParam(required = false) String type,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(itemService.filterByDishType(type, pageable));
    }

    // Combined search and filter (main combined requirement)
    @GetMapping("/items/combined")
    public ResponseEntity<Page<ItemDto>> searchAndFilter(
            @RequestParam(required = false) String q,           // Search query
            @RequestParam(required = false) String type,        // Dish type filter
            @RequestParam(required = false) Boolean bestSeller, // Best seller filter
            @RequestParam(required = false) Boolean available,  // Available filter
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        
        return ResponseEntity.ok(itemService.searchAndFilter(q, type, bestSeller, available, pageable));
    }

    // Get best seller items
    @GetMapping("/items/best-sellers")
    public ResponseEntity<List<ItemDto>> getBestSellers() {
        return ResponseEntity.ok(itemService.getBestSellerItems());
    }

    // Increment order count when item is ordered
    @PostMapping("/items/{id}/order")
    public ResponseEntity<Void> orderItem(@PathVariable Long id) {
        itemService.incrementOrderCount(id);
        return ResponseEntity.ok().build();
    }

    // Main business requirement: Second-highest calorie meal per category
    @Operation(summary = "Get second-highest calorie meal per category", 
               description = "🎯 MAIN BUSINESS REQUIREMENT: Retrieve the second-highest calorie meal for each category (Soup, Rice, Others)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved second-highest calorie meals")
    })
    @GetMapping("/second-highest-calorie")
    public ResponseEntity<Map<String, SecondHighestCalorieDto>> getSecondHighestCaloriePerCategory() {
        return ResponseEntity.ok(calorieAnalysisService.getSecondHighestCaloriePerCategory());
    }

    // Get second-highest calorie for specific category
    @GetMapping("/second-highest-calorie/{categoryName}")
    public ResponseEntity<SecondHighestCalorieDto> getSecondHighestCalorieByCategory(
            @PathVariable String categoryName) {
        try {
            return ResponseEntity.ok(calorieAnalysisService.getSecondHighestCalorieByCategory(categoryName));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get all categories (for frontend display)
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    // Get item count per category (for category cards display)
    @GetMapping("/categories/counts")
    public ResponseEntity<Map<String, Long>> getCategoryCounts() {
        return ResponseEntity.ok(categoryService.getItemCountsByCategory());
    }

    // Manually refresh best sellers (admin endpoint)
    @PutMapping("/best-sellers/refresh")
    public ResponseEntity<Void> refreshBestSellers() {
        itemService.refreshBestSellers();
        return ResponseEntity.ok().build();
    }

    // Filter by availability
    @GetMapping("/items/filter/availability")
    public ResponseEntity<Page<ItemDto>> filterByAvailability(
            @RequestParam Boolean available,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(itemService.filterByAvailability(available, pageable));
    }

    // Filter by calorie range
    @GetMapping("/items/filter/calories")
    public ResponseEntity<Page<ItemDto>> filterByCalorieRange(
            @RequestParam(required = false) Integer minCalories,
            @RequestParam(required = false) Integer maxCalories,
            @PageableDefault(size = 20, sort = "calories") Pageable pageable) {
        return ResponseEntity.ok(itemService.filterByCalorieRange(minCalories, maxCalories, pageable));
    }

    // Validation endpoint - check which categories have enough items for analysis
    @GetMapping("/category-validation")
    public ResponseEntity<Map<String, Integer>> getCategoryItemCounts() {
        return ResponseEntity.ok(calorieAnalysisService.getCategoryItemCounts());
    }

    // Create new item (admin endpoint)
    @PostMapping("/items")
    public ResponseEntity<ItemDto> createItem(@RequestBody ItemDto itemDto) {
        try {
            ItemDto createdItem = itemService.createItem(itemDto);
            return ResponseEntity.ok(createdItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Update item (admin endpoint)
    @PutMapping("/items/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable Long id, @RequestBody ItemDto itemDto) {
        try {
            ItemDto updatedItem = itemService.updateItem(id, itemDto);
            return ResponseEntity.ok(updatedItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Delete item (admin endpoint)
    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        try {
            itemService.deleteItem(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
