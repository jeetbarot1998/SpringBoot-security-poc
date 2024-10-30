package com.example.jwt_demo.controller.vendor;

import com.example.jwt_demo.model.AuthResponse;
import com.example.jwt_demo.model.FoodItem;
import com.example.jwt_demo.model.Vendor;
import com.example.jwt_demo.service.FoodItemService;
import com.example.jwt_demo.service.VendorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/vendor/v1")
@RequiredArgsConstructor
@Tag(name = "Food Item Management", description = "APIs for managing food items")
public class FoodItemController {
    private final FoodItemService foodItemService;
    private final VendorService vendorService;

    @Operation(summary = "Create new food item",
            description = "Creates a new food item for logged in vendor",
            security = { @SecurityRequirement(name = "Bearer Authentication") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Food item created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PostMapping("/foods")
    @ResponseStatus(HttpStatus.CREATED)
    public FoodItem createFood(
            @Parameter(description = "Food item details", required = true)
            @Valid @RequestBody FoodItem foodItem,
            Principal principal) {
        Vendor vendor = (Vendor) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return foodItemService.create(foodItem, vendor.getId());
    }

    @Operation(summary = "Update food item",
            description = "Updates food item for logged in vendor",
            security = { @SecurityRequirement(name = "Bearer Authentication") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food item updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "404", description = "Food item not found")
    })
    @PutMapping("/foods/{foodId}")
    public FoodItem updateFood(
            @Parameter(description = "ID of the food item", required = true)
            @PathVariable Long foodId,
            @Parameter(description = "Updated food item details", required = true)
            @Valid @RequestBody FoodItem foodItem,
            Principal principal) {
        Vendor vendor = vendorService.getCurrentVendor(principal);
        return foodItemService.update(foodId, foodItem, vendor.getId());
    }

    @Operation(summary = "Delete food item",
            description = "Deletes food item for logged in vendor",
            security = { @SecurityRequirement(name = "Bearer Authentication") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Food item deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "404", description = "Food item not found")
    })
    @DeleteMapping("/foods/{foodId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFood(
            @Parameter(description = "ID of the food item", required = true)
            @PathVariable Long foodId,
            Principal principal) {
        Vendor vendor = vendorService.getCurrentVendor(principal);
        foodItemService.delete(foodId, vendor.getId());
    }

    @Operation(summary = "Get all foods by vendor",
            description = "Retrieves all food items for logged in vendor",
            security = { @SecurityRequirement(name = "Bearer Authentication") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved food items",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = FoodItem.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/vendor/foods")
    public List<FoodItem> getVendorFoods(Principal principal) {
        Vendor vendor = vendorService.getCurrentVendor(principal);
        return foodItemService.getAllByVendor(vendor.getId());
    }

    @Operation(summary = "Get food item details",
            description = "Retrieves details of a specific food item",
            security = { @SecurityRequirement(name = "Bearer Authentication") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved food item"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "404", description = "Food item not found")
    })
    @GetMapping("/vendor/foods/{foodId}")
    public FoodItem getFoodItem(
            @Parameter(description = "ID of the food item", required = true)
            @PathVariable Long foodId,
            Principal principal) {
        Vendor vendor = vendorService.getCurrentVendor(principal);
        return foodItemService.getOne(foodId, vendor.getId());
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
    @GetMapping("/test")
    public ResponseEntity<?> testVendor(Principal principal) {
        try {
            Vendor vendor = vendorService.getCurrentVendor(principal);
            System.out.println(vendor);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new AuthResponse("Vendor authentication successful", null));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new AuthResponse("Authentication failed", null));
        }
    }
}