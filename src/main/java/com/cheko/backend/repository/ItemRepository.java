package com.cheko.backend.repository;

import com.cheko.backend.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // Find all items with pagination
    @Query("SELECT i FROM Item i WHERE i.deletedAt IS NULL ORDER BY i.name ASC")
    Page<Item> findAllItems(Pageable pageable);

    // Search by name OR description (case-insensitive) - Main search requirement
    @Query("SELECT i FROM Item i WHERE i.deletedAt IS NULL AND " +
           "(LOWER(i.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(i.description) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "ORDER BY i.name ASC")
    Page<Item> searchByNameOrDescription(@Param("query") String query, Pageable pageable);

    // Filter by category name (Soup, Rice, Others) - Main filter requirement
    @Query("SELECT i FROM Item i JOIN i.category c WHERE i.deletedAt IS NULL AND " +
           "LOWER(c.name) = LOWER(:categoryName) " +
           "ORDER BY i.name ASC")
    Page<Item> findByCategoryName(@Param("categoryName") String categoryName, Pageable pageable);

    // Combined search and filter - Main combined requirement
    @Query("SELECT i FROM Item i JOIN i.category c WHERE i.deletedAt IS NULL AND " +
           "(:query IS NULL OR LOWER(i.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(i.description) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:categoryName IS NULL OR LOWER(c.name) = LOWER(:categoryName)) AND " +
           "(:bestSeller IS NULL OR i.isBestSeller = :bestSeller) AND " +
           "(:available IS NULL OR i.isAvailable = :available) " +
           "ORDER BY i.name ASC")
    Page<Item> searchAndFilter(@Param("query") String query, 
                              @Param("categoryName") String categoryName,
                              @Param("bestSeller") Boolean bestSeller,
                              @Param("available") Boolean available,
                              Pageable pageable);

    // Get best seller items
    @Query("SELECT i FROM Item i WHERE i.deletedAt IS NULL AND i.isBestSeller = true ORDER BY i.totalOrders DESC, i.name ASC")
    List<Item> findBestSellers();

    // Get second-highest calorie item per category using LIMIT/OFFSET - Main business requirement
    @Query(value = "SELECT i.* FROM items i " +
           "JOIN categories c ON i.category_id = c.id " +
           "WHERE c.name = :categoryName AND i.deleted_at IS NULL AND i.calories IS NOT NULL " +
           "ORDER BY i.calories DESC " +
           "LIMIT 1 OFFSET 1", nativeQuery = true)
    Optional<Item> findSecondHighestCalorieByCategoryName(@Param("categoryName") String categoryName);

    // Get second-highest calorie item by category ID
    @Query(value = "SELECT i.* FROM items i " +
           "WHERE i.category_id = :categoryId AND i.deleted_at IS NULL AND i.calories IS NOT NULL " +
           "ORDER BY i.calories DESC " +
           "LIMIT 1 OFFSET 1", nativeQuery = true)
    Optional<Item> findSecondHighestCalorieByCategoryId(@Param("categoryId") Long categoryId);

    // Get all category names that have at least 2 items (for second-highest calorie validation)
    @Query(value = "SELECT c.name FROM categories c " +
           "JOIN items i ON c.id = i.category_id " +
           "WHERE i.deleted_at IS NULL AND i.calories IS NOT NULL " +
           "GROUP BY c.id, c.name " +
           "HAVING COUNT(i.id) >= 2", nativeQuery = true)
    List<String> findCategoriesWithAtLeastTwoItems();

    // Get item count per category (for category cards display)
    @Query(value = "SELECT c.name, COUNT(i.id) FROM categories c " +
           "LEFT JOIN items i ON c.id = i.category_id AND i.deleted_at IS NULL " +
           "WHERE c.deleted_at IS NULL " +
           "GROUP BY c.id, c.name " +
           "ORDER BY c.name", nativeQuery = true)
    List<Object[]> getCategoryItemCounts();

    // Get top N items by total orders (for best seller calculation)
    @Query("SELECT i FROM Item i WHERE i.deletedAt IS NULL ORDER BY i.totalOrders DESC")
    List<Item> findTopItemsByOrders(Pageable pageable);

    // Reset all best sellers (for recalculation)
    @Modifying
    @Query("UPDATE Item i SET i.isBestSeller = false WHERE i.deletedAt IS NULL")
    void resetAllBestSellers();

    // Set best seller status for specific items
    @Modifying
    @Query("UPDATE Item i SET i.isBestSeller = true WHERE i.id IN :itemIds")
    void setBestSellers(@Param("itemIds") List<Long> itemIds);

    // Filter by availability
    @Query("SELECT i FROM Item i WHERE i.deletedAt IS NULL AND i.isAvailable = :available ORDER BY i.name ASC")
    Page<Item> findByAvailability(@Param("available") Boolean available, Pageable pageable);

    // Filter by calorie range
    @Query("SELECT i FROM Item i WHERE i.deletedAt IS NULL AND " +
           "(:minCalories IS NULL OR i.calories >= :minCalories) AND " +
           "(:maxCalories IS NULL OR i.calories <= :maxCalories) " +
           "ORDER BY i.calories DESC")
    Page<Item> findByCalorieRange(@Param("minCalories") Integer minCalories, 
                                 @Param("maxCalories") Integer maxCalories, 
                                 Pageable pageable);

    // Check if item exists by name
    @Query("SELECT COUNT(i) > 0 FROM Item i WHERE i.deletedAt IS NULL AND LOWER(i.name) = LOWER(:name)")
    boolean existsByNameIgnoreCase(@Param("name") String name);
}