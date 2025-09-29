package com.cheko.backend.service;

import com.cheko.backend.dto.LocationDto;
import com.cheko.backend.dto.MapMarkerDto;
import com.cheko.backend.model.Location;
import com.cheko.backend.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    // Get all locations for map display
    @Transactional(readOnly = true)
    public List<MapMarkerDto> getAllMapMarkers() {
        return locationRepository.findAllActiveLocations()
                .stream()
                .map(this::convertToMapMarkerDto)
                .collect(Collectors.toList());
    }

    // Get location by branch ID
    @Transactional(readOnly = true)
    public Optional<LocationDto> getLocationByBranchId(Long branchId) {
        return locationRepository.findByBranchId(branchId)
                .map(this::convertToLocationDto);
    }

    // Global search for map markers
    @Transactional(readOnly = true)
    public List<MapMarkerDto> searchMapMarkers(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllMapMarkers();
        }
        return locationRepository.searchGlobal(query.trim())
                .stream()
                .map(this::convertToMapMarkerDto)
                .collect(Collectors.toList());
    }

    // Search markers by branch name
    @Transactional(readOnly = true)
    public List<MapMarkerDto> searchMarkersByBranchName(String query) {
        return locationRepository.searchByBranchName(query)
                .stream()
                .map(this::convertToMapMarkerDto)
                .collect(Collectors.toList());
    }

    // Search markers by address
    @Transactional(readOnly = true)
    public List<MapMarkerDto> searchMarkersByAddress(String query) {
        return locationRepository.searchByAddress(query)
                .stream()
                .map(this::convertToMapMarkerDto)
                .collect(Collectors.toList());
    }

    // Filter markers by city
    @Transactional(readOnly = true)
    public List<MapMarkerDto> filterMarkersByCity(String city) {
        return locationRepository.findByCity(city)
                .stream()
                .map(this::convertToMapMarkerDto)
                .collect(Collectors.toList());
    }

    // Filter markers by state
    @Transactional(readOnly = true)
    public List<MapMarkerDto> filterMarkersByState(String state) {
        return locationRepository.findByState(state)
                .stream()
                .map(this::convertToMapMarkerDto)
                .collect(Collectors.toList());
    }

    // Combined search and filter for map
    @Transactional(readOnly = true)
    public List<MapMarkerDto> searchAndFilterMapMarkers(String query, String city, String state, Boolean isActive) {
        return locationRepository.searchAndFilter(query, city, state, isActive)
                .stream()
                .map(this::convertToMapMarkerDto)
                .collect(Collectors.toList());
    }

    // Find nearby markers using radius
    @Transactional(readOnly = true)
    public List<MapMarkerDto> getNearbyMarkers(BigDecimal lat, BigDecimal lng, Double radiusKm) {
        return locationRepository.findByRadius(lat, lng, radiusKm)
                .stream()
                .map(this::convertToMapMarkerDto)
                .collect(Collectors.toList());
    }

    // Get unique cities for filter options
    @Transactional(readOnly = true)
    public List<String> getAvailableCities() {
        return locationRepository.findDistinctCities();
    }

    // Get unique states for filter options
    @Transactional(readOnly = true)
    public List<String> getAvailableStates() {
        return locationRepository.findDistinctStates();
    }

    // Convert Location to MapMarkerDto for map display
    private MapMarkerDto convertToMapMarkerDto(Location location) {
        MapMarkerDto dto = new MapMarkerDto();
        dto.setId(location.getId());
        dto.setBranchName(location.getBranch().getName());
        dto.setAddress(location.getAddress());
        dto.setDescription(location.getBranch().getDescription());
        dto.setPhone(location.getBranch().getPhone());
        dto.setOpeningHours(location.getBranch().getOpeningHours());
        dto.setLatitude(location.getLatitude());
        dto.setLongitude(location.getLongitude());
        dto.setCity(location.getCity());
        dto.setState(location.getState());
        dto.setMapZoomLevel(location.getMapZoomLevel());
        dto.setIsActive(location.getBranch().getIsActive());
        
        // Create popup content for tooltip
        dto.setPopupContent(createPopupContent(location));
        
        return dto;
    }

    // Convert Location to LocationDto
    private LocationDto convertToLocationDto(Location location) {
        LocationDto dto = new LocationDto();
        dto.setId(location.getId());
        dto.setBranchId(location.getBranch().getId());
        dto.setBranchName(location.getBranch().getName());
        dto.setAddress(location.getAddress());
        dto.setLatitude(location.getLatitude());
        dto.setLongitude(location.getLongitude());
        dto.setCity(location.getCity());
        dto.setState(location.getState());
        dto.setPostalCode(location.getPostalCode());
        dto.setCountry(location.getCountry());
        dto.setMapZoomLevel(location.getMapZoomLevel());
        dto.setCreatedAt(location.getCreatedAt());
        dto.setUpdatedAt(location.getUpdatedAt());
        return dto;
    }

    // Create HTML popup content for map tooltip
    private String createPopupContent(Location location) {
        return String.format(
            "<div class='map-tooltip'>" +
            "<h3>%s</h3>" +
            "<p>%s</p>" +
            "<p><strong>Phone:</strong> %s</p>" +
            "<p><strong>Hours:</strong> %s</p>" +
            "</div>",
            location.getBranch().getName(),
            location.getAddress(),
            location.getBranch().getPhone() != null ? location.getBranch().getPhone() : "N/A",
            location.getBranch().getOpeningHours() != null ? location.getBranch().getOpeningHours() : "N/A"
        );
    }
}

