package com.example.jwt_demo.controller;

import com.example.jwt_demo.model.AuthRequest;
import com.example.jwt_demo.model.SignupRequest;
import com.example.jwt_demo.model.Vendor;
import com.example.jwt_demo.model.AuthResponse;
import com.example.jwt_demo.service.VendorDetailsService;
import com.example.jwt_demo.service.JwtUtil;
import com.example.jwt_demo.service.VendorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendor")
@Tag(name = "Vendor Authentication", description = "Vendor authentication management APIs")
@CrossOrigin(origins = "*", maxAge = 3600)
public class VendorAuthController {

    private final AuthenticationManager authenticationManager;
    private final VendorDetailsService vendorDetailsService;
    private final VendorService vendorService;
    private final JwtUtil jwtUtil;

    public VendorAuthController(AuthenticationManager authenticationManager,
                                @Qualifier("vendorDetailsService") VendorDetailsService vendorDetailsService,
                                VendorService vendorService,
                                JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.vendorDetailsService = vendorDetailsService;
        this.vendorService = vendorService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "Register a new vendor",
            description = "Creates a new vendor account with the provided credentials",
            security = {})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vendor registered successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AuthResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input or email already exists",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AuthResponse.class)
                    )
            )
    })
    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @Parameter(description = "Signup credentials", required = true)
            @RequestBody SignupRequest signupRequest) {
        try {
            Vendor vendor = vendorService.signup(signupRequest);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new AuthResponse("Vendor registered successfully.", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new AuthResponse(e.getMessage(), null));
        }
    }

    @Operation(summary = "Authenticate vendor",
            description = "Authenticates vendor credentials and returns JWT token",
            security = {})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AuthResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials or vendor not verified",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AuthResponse.class)
                    )
            )
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest loginRequest) {
        try {
            UserDetails vendorDetails = vendorDetailsService.loadUserByUsername(loginRequest.getEmail());


            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            String token = jwtUtil.generateToken(vendorDetails);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new AuthResponse("Authentication successful", token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new AuthResponse("Invalid username or password", null));
        }
    }

    @Operation(summary = "Test API Vendor",
            description = "Test API for vendor API",
            security = { @SecurityRequirement(name = "Bearer Authentication") })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AuthResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials or vendor not verified",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AuthResponse.class)
                    )
            )
    })
    @PostMapping("/test")
    public ResponseEntity<?> test(@RequestBody AuthRequest loginRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(authentication);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new AuthResponse("Vendor Test API", null));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new AuthResponse("Invalid username or password", null));
        }
    }

}