package com.example.jwt_demo.service;

import com.example.jwt_demo.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Qualifier("vendorDetailsService")
public class VendorDetailsService implements UserDetailsService {

    private final VendorRepository vendorRepository;

    public VendorDetailsService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return vendorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Vendor not found with email: " + email));
    }
}