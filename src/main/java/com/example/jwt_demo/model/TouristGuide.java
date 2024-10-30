package com.example.jwt_demo.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tourist_guides")
@Data
@NoArgsConstructor
public class TouristGuide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @NotBlank
    private String name;
    private String description;
    private Double entryFee;
    private String location;
    private Double latitude;
    private Double longitude;
    private String imageUrl;
    private Boolean isActive = true;

    @Column(name = "visiting_hours")
    private String visitingHours;

    @ElementCollection
    private List<String> activities;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}