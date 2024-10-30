package com.example.jwt_demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "vendors")
@Data
@NoArgsConstructor
public class Vendor implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String businessName;

    @NotBlank
    private String vendorType; // FOOD, EVENTS, STAY, TOURIST_GUIDE

    private String description;

    @NotBlank
    private String role;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "business_address")
    private String businessAddress;

    // Location coordinates for map view
    private Double latitude;
    private Double longitude;

    // Business registration details
    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "tax_id")
    private String taxId;

    // Validation fields
    @Column(name = "validation_flag")
    private Boolean validationFlag = false;

    @Column(name = "validation_video_url")
    private String validationVideoUrl;

    @Column(name = "validation_status")
    @Enumerated(EnumType.STRING)
    private ValidationStatus validationStatus = ValidationStatus.PENDING;

    // Business hours and availability
    @Column(name = "business_hours")
    private String businessHours;

    // Rating and reviews
    private Double rating;

    @Column(name = "review_count")
    private Integer reviewCount = 0;

    // Timestamps
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Additional business-specific fields based on vendor type
//    @Column(columnDefinition = "jsonb")
//    private String additionalDetails; // Stored as JSON

    public enum ValidationStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // UserDetails implementation
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("VENDOR"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}