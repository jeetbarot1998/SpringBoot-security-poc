package com.example.jwt_demo.config;

public class Views {
    public static class Public {}               // Base view
    public static class Internal extends Public {} // Extended view
    public static class Admin extends Internal {}  // Admin view
}