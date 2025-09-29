package com.cheko.backend.dto;

import java.math.BigDecimal;

public class MapMarkerDto {
    private Long id;
    private String branchName;
    private String address;
    private String description;
    private String phone;
    private String openingHours;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String city;
    private String state;
    private Integer mapZoomLevel;
    private Boolean isActive;
    
    // Mapbox-specific fields
    private String markerColor = "#FF6B35"; // Cheko brand color
    private String markerIcon = "restaurant";
    private String popupContent; // HTML content for tooltip

    // Constructors
    public MapMarkerDto() {}

    public MapMarkerDto(Long id, String branchName, String address, BigDecimal latitude, 
                       BigDecimal longitude, String phone, String openingHours) {
        this.id = id;
        this.branchName = branchName;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.openingHours = openingHours;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getMapZoomLevel() {
        return mapZoomLevel;
    }

    public void setMapZoomLevel(Integer mapZoomLevel) {
        this.mapZoomLevel = mapZoomLevel;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getMarkerColor() {
        return markerColor;
    }

    public void setMarkerColor(String markerColor) {
        this.markerColor = markerColor;
    }

    public String getMarkerIcon() {
        return markerIcon;
    }

    public void setMarkerIcon(String markerIcon) {
        this.markerIcon = markerIcon;
    }

    public String getPopupContent() {
        return popupContent;
    }

    public void setPopupContent(String popupContent) {
        this.popupContent = popupContent;
    }
}

