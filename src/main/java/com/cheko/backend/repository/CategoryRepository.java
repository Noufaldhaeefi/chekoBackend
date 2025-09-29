package com.cheko.backend.repository;

import com.cheko.backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Find all categories ordered by name
    @Query("SELECT c FROM Category c WHERE c.deletedAt IS NULL ORDER BY c.name ASC")
    List<Category> findAllOrderedByName();

    // Find category by name (case-insensitive)
    @Query("SELECT c FROM Category c WHERE c.deletedAt IS NULL AND LOWER(c.name) = LOWER(:name)")
    Optional<Category> findByNameIgnoreCase(@Param("name") String name);

    // Get item count per category for category cards display
    @Query(value = "SELECT c.name, COUNT(i.id) FROM categories c " +
           "LEFT JOIN items i ON c.id = i.category_id AND i.deleted_at IS NULL " +
           "WHERE c.deleted_at IS NULL " +
           "GROUP BY c.id, c.name " +
           "ORDER BY c.name", nativeQuery = true)
    List<Object[]> getCategoryItemCounts();

    // Find categories with at least N items (for validation)
    @Query(value = "SELECT c.name FROM categories c " +
           "JOIN items i ON c.id = i.category_id " +
           "WHERE c.deleted_at IS NULL AND i.deleted_at IS NULL AND i.calories IS NOT NULL " +
           "GROUP BY c.id, c.name " +
           "HAVING COUNT(i.id) >= :minItems", nativeQuery = true)
    List<String> findCategoriesWithAtLeastItems(@Param("minItems") int minItems);

    // Check if category exists by name
    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE c.deletedAt IS NULL AND LOWER(c.name) = LOWER(:name)")
    boolean existsByNameIgnoreCase(@Param("name") String name);
}