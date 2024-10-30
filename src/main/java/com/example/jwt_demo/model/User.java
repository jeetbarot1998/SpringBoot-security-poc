package com.example.jwt_demo.model;

import com.example.jwt_demo.config.Views;
import com.fasterxml.jackson.annotation.JsonView;
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
@Table(name = "users")
@Data
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.Public.class})
    private Long id;

    @NotBlank
    @Email
    @Column(unique = true)
    @JsonView({Views.Public.class})
    private String email;

    @NotBlank
    @JsonView(Views.Internal.class)  // Only visible in Internal and Admin views
    private String password;

    @NotBlank
    @JsonView({Views.Public.class})
    private String role;

    @JsonView({Views.Public.class})
    private String firstName;

    @JsonView({Views.Public.class})
    private String lastName;

    @Column(name = "validation_flag")
    private Boolean validationFlag = false;

    @Column(name = "validation_video_url")
    private String validationVideoUrl;

    @Column(name = "created_at")
    @JsonView({Views.Public.class})
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
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