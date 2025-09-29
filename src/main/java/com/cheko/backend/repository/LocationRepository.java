package com.cheko.backend.repository;

import com.cheko.backend.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    // Find all locations for map display
    @Query("SELECT l FROM Location l JOIN FETCH l.branch b WHERE l.deletedAt IS NULL AND b.isActive = true ORDER BY b.name ASC")
    List<Location> findAllActiveLocations();

    // Find location by branch ID
    @Query("SELECT l FROM Location l WHERE l.deletedAt IS NULL AND l.branch.id = :branchId")
    Optional<Location> findByBranchId(@Param("branchId") Long branchId);

    // Global search across branch name, address, and description
    @Query("SELECT l FROM Location l JOIN l.branch b WHERE l.deletedAt IS NULL AND b.isActive = true AND " +
           "(LOWER(b.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(l.address) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(b.description) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "ORDER BY b.name ASC")
    List<Location> searchGlobal(@Param("query") String query);

    // Search by branch name
    @Query("SELECT l FROM Location l JOIN l.branch b WHERE l.deletedAt IS NULL AND b.isActive = true AND " +
           "LOWER(b.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "ORDER BY b.name ASC")
    List<Location> searchByBranchName(@Param("query") String query);

    // Search by address
    @Query("SELECT l FROM Location l JOIN l.branch b WHERE l.deletedAt IS NULL AND b.isActive = true AND " +
           "LOWER(l.address) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "ORDER BY b.name ASC")
    List<Location> searchByAddress(@Param("query") String query);

    // Filter by city
    @Query("SELECT l FROM Location l JOIN l.branch b WHERE l.deletedAt IS NULL AND b.isActive = true AND " +
           "LOWER(l.city) = LOWER(:city) " +
           "ORDER BY b.name ASC")
    List<Location> findByCity(@Param("city") String city);

    // Filter by state
    @Query("SELECT l FROM Location l JOIN l.branch b WHERE l.deletedAt IS NULL AND b.isActive = true AND " +
           "LOWER(l.state) = LOWER(:state) " +
           "ORDER BY b.name ASC")
    List<Location> findByState(@Param("state") String state);

    // Combined search and filter for map
    @Query("SELECT l FROM Location l JOIN l.branch b WHERE l.deletedAt IS NULL AND " +
           "(:query IS NULL OR " +
           " LOWER(b.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           " LOWER(l.address) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           " LOWER(b.description) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:city IS NULL OR LOWER(l.city) = LOWER(:city)) AND " +
           "(:state IS NULL OR LOWER(l.state) = LOWER(:state)) AND " +
           "(:isActive IS NULL OR b.isActive = :isActive) " +
           "ORDER BY b.name ASC")
    List<Location> searchAndFilter(@Param("query") String query,
                                  @Param("city") String city,
                                  @Param("state") String state,
                                  @Param("isActive") Boolean isActive);

    // Find nearby locations using Haversine formula (radius in kilometers)
    @Query(value = "SELECT l.* FROM locations l " +
           "JOIN branches b ON l.branch_id = b.id " +
           "WHERE l.deleted_at IS NULL AND b.is_active = true AND " +
           "(6371 * acos(cos(radians(:lat)) * cos(radians(CAST(l.latitude AS DOUBLE PRECISION))) * " +
           "cos(radians(CAST(l.longitude AS DOUBLE PRECISION)) - radians(:lng)) + " +
           "sin(radians(:lat)) * sin(radians(CAST(l.latitude AS DOUBLE PRECISION))))) <= :radiusKm " +
           "ORDER BY (6371 * acos(cos(radians(:lat)) * cos(radians(CAST(l.latitude AS DOUBLE PRECISION))) * " +
           "cos(radians(CAST(l.longitude AS DOUBLE PRECISION)) - radians(:lng)) + " +
           "sin(radians(:lat)) * sin(radians(CAST(l.latitude AS DOUBLE PRECISION)))))", nativeQuery = true)
    List<Location> findByRadius(@Param("lat") BigDecimal latitude, 
                               @Param("lng") BigDecimal longitude, 
                               @Param("radiusKm") Double radiusKm);

    // Get unique cities for filter options
    @Query("SELECT DISTINCT l.city FROM Location l JOIN l.branch b WHERE l.deletedAt IS NULL AND b.isActive = true AND l.city IS NOT NULL ORDER BY l.city")
    List<String> findDistinctCities();

    // Get unique states for filter options
    @Query("SELECT DISTINCT l.state FROM Location l JOIN l.branch b WHERE l.deletedAt IS NULL AND b.isActive = true AND l.state IS NOT NULL ORDER BY l.state")
    List<String> findDistinctStates();
}