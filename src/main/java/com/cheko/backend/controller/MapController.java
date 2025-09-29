package com.cheko.backend.controller;

import com.cheko.backend.dto.MapMarkerDto;
import com.cheko.backend.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/map")
@CrossOrigin(origins = "*") // Allow all origins for development
@Tag(name = "Map & Location Services", description = "APIs for Mapbox GL JS integration - branch locations, search, and filtering")
public class MapController {

    @Autowired
    private LocationService locationService;

    // Get all markers for initial map load
    @Operation(summary = "Get all map markers", 
               description = "🗺️ Get all branch locations as markers for Mapbox GL JS map display")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all map markers")
    })
    @GetMapping("/markers")
    public ResponseEntity<List<MapMarkerDto>> getAllMarkers() {
        return ResponseEntity.ok(locationService.getAllMapMarkers());
    }

    // Search markers globally (branch name, address, description)
    @GetMapping("/markers/search")
    public ResponseEntity<List<MapMarkerDto>> searchMarkers(
            @RequestParam(required = false) String q) {
        return ResponseEntity.ok(locationService.searchMapMarkers(q));
    }

    // Search markers by branch name
    @GetMapping("/markers/search/branch")
    public ResponseEntity<List<MapMarkerDto>> searchMarkersByBranch(
            @RequestParam String q) {
        return ResponseEntity.ok(locationService.searchMarkersByBranchName(q));
    }

    // Search markers by address
    @GetMapping("/markers/search/address")
    public ResponseEntity<List<MapMarkerDto>> searchMarkersByAddress(
            @RequestParam String q) {
        return ResponseEntity.ok(locationService.searchMarkersByAddress(q));
    }

    // Filter markers by city
    @GetMapping("/markers/filter")
    public ResponseEntity<List<MapMarkerDto>> filterMarkers(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) Boolean active) {
        
        if (city != null) {
            return ResponseEntity.ok(locationService.filterMarkersByCity(city));
        } else if (state != null) {
            return ResponseEntity.ok(locationService.filterMarkersByState(state));
        } else {
            // If no specific filter, return all with active filter
            return ResponseEntity.ok(locationService.searchAndFilterMapMarkers(null, null, null, active));
        }
    }

    // Combined search and filter for map
    @GetMapping("/markers/combined")
    public ResponseEntity<List<MapMarkerDto>> searchAndFilterMarkers(
            @RequestParam(required = false) String q,      // Search query
            @RequestParam(required = false) String city,   // City filter
            @RequestParam(required = false) String state,  // State filter  
            @RequestParam(required = false) Boolean active) { // Active filter
        
        return ResponseEntity.ok(locationService.searchAndFilterMapMarkers(q, city, state, active));
    }

    // Find nearby markers using radius
    @GetMapping("/markers/nearby")
    public ResponseEntity<List<MapMarkerDto>> getNearbyMarkers(
            @RequestParam BigDecimal lat,
            @RequestParam BigDecimal lng,
            @RequestParam(defaultValue = "10") Double radius) {
        
        return ResponseEntity.ok(locationService.getNearbyMarkers(lat, lng, radius));
    }

    // Get unique cities for filter dropdown
    @GetMapping("/cities")
    public ResponseEntity<List<String>> getAvailableCities() {
        return ResponseEntity.ok(locationService.getAvailableCities());
    }

    // Get unique states for filter dropdown
    @GetMapping("/states")
    public ResponseEntity<List<String>> getAvailableStates() {
        return ResponseEntity.ok(locationService.getAvailableStates());
    }
}
