package com.example.jwt_demo.service;

import com.example.jwt_demo.model.Vendor;
import com.example.jwt_demo.model.SignupRequest;
import com.example.jwt_demo.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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

    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }
}