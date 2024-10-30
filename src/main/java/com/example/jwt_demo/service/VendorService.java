package com.example.jwt_demo.service;

import com.example.jwt_demo.model.Vendor;
import com.example.jwt_demo.model.SignupRequest;
import com.example.jwt_demo.repository.vendor.VendorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@Transactional
public class VendorService {
    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Vendor signup(SignupRequest signupRequest) throws Exception {
        if (vendorRepository.existsByEmail(signupRequest.getEmail())) {
            throw new Exception("Email already exists");
        }

        Vendor vendor = new Vendor();
        vendor.setEmail(signupRequest.getEmail());
        vendor.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        vendor.setRole("VENDOR"); // Default role
//        vendor.setValidationFlag(false); // Needs admin validation
//        vendor.setValidationStatus(Vendor.ValidationStatus.PENDING);

        return vendorRepository.save(vendor);
    }

    public Vendor getCurrentVendor(Principal principal) {
        String email = ((UsernamePasswordAuthenticationToken) principal).getName();
        return vendorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Vendor not found"));
    }

    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }
}