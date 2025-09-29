package com.cheko.backend.repository;

import com.cheko.backend.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

    // Find all active branches
    @Query("SELECT b FROM Branch b WHERE b.deletedAt IS NULL AND b.isActive = true ORDER BY b.name ASC")
    List<Branch> findAllActiveBranches();

    // Search branches by name
    @Query("SELECT b FROM Branch b WHERE b.deletedAt IS NULL AND " +
           "LOWER(b.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "ORDER BY b.name ASC")
    List<Branch> searchByName(@Param("query") String query);

    // Find branches by active status
    @Query("SELECT b FROM Branch b WHERE b.deletedAt IS NULL AND b.isActive = :isActive ORDER BY b.name ASC")
    List<Branch> findByActiveStatus(@Param("isActive") Boolean isActive);

    // Check if branch exists by name
    @Query("SELECT COUNT(b) > 0 FROM Branch b WHERE b.deletedAt IS NULL AND LOWER(b.name) = LOWER(:name)")
    boolean existsByNameIgnoreCase(@Param("name") String name);
}