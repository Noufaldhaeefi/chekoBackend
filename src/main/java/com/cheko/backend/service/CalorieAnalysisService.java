package com.cheko.backend.service;

import com.cheko.backend.dto.SecondHighestCalorieDto;
import com.cheko.backend.model.Item;
import com.cheko.backend.repository.CategoryRepository;
import com.cheko.backend.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CalorieAnalysisService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Main business requirement: Get second-highest calorie meal per category
    public Map<String, SecondHighestCalorieDto> getSecondHighestCaloriePerCategory() {
        // Get all categories that have at least 2 items
        List<String> validCategories = itemRepository.findCategoriesWithAtLeastTwoItems();
        Map<String, SecondHighestCalorieDto> result = new HashMap<>();

        for (String categoryName : validCategories) {
            Optional<Item> secondHighestItem = itemRepository.findSecondHighestCalorieByCategoryName(categoryName);

            if (secondHighestItem.isPresent()) {
                Item item = secondHighestItem.get();
                SecondHighestCalorieDto dto = SecondHighestCalorieDto.builder()
                        .itemId(item.getId())
                        .itemName(item.getName())
                        .description(item.getDescription())
                        .calories(item.getCalories())
                        .price(item.getPrice())
                        .imageUrl(item.getImageUrl())
                        .categoryName(categoryName)
                        .isAvailable(item.getIsAvailable())
                        .isBestSeller(item.getIsBestSeller())
                        .build();

                result.put(categoryName, dto);
            }
        }

        return result;
    }

    // Get second-highest calorie for specific category
    public SecondHighestCalorieDto getSecondHighestCalorieByCategory(String categoryName) {
        Optional<Item> secondHighestItem = itemRepository.findSecondHighestCalorieByCategoryName(categoryName);

        if (secondHighestItem.isEmpty()) {
            throw new RuntimeException(
                "No second-highest calorie item found for category: " + categoryName + 
                ". Category may not exist or may have less than 2 items with calorie data.");
        }

        Item item = secondHighestItem.get();
        return SecondHighestCalorieDto.builder()
                .itemId(item.getId())
                .itemName(item.getName())
                .description(item.getDescription())
                .calories(item.getCalories())
                .price(item.getPrice())
                .imageUrl(item.getImageUrl())
                .categoryName(categoryName)
                .isAvailable(item.getIsAvailable())
                .isBestSeller(item.getIsBestSeller())
                .build();
    }

    // Validation method - check which categories have enough items for second-highest analysis
    public Map<String, Integer> getCategoryItemCounts() {
        List<Object[]> results = itemRepository.getCategoryItemCounts();
        Map<String, Integer> counts = new HashMap<>();

        for (Object[] result : results) {
            String categoryName = (String) result[0];
            Long count = ((Number) result[1]).longValue();
            counts.put(categoryName, count.intValue());
        }

        return counts;
    }

    // Get categories that have at least 2 items with calorie data
    public List<String> getValidCategoriesForCalorieAnalysis() {
        return itemRepository.findCategoriesWithAtLeastTwoItems();
    }

    // Check if category has enough items for second-highest calorie analysis
    public boolean canAnalyzeCategory(String categoryName) {
        return itemRepository.findSecondHighestCalorieByCategoryName(categoryName).isPresent();
    }
}

