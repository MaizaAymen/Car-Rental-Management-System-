package com.example.demo.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping("/echo")
    public String echoJson(@RequestBody String json) {
        return "Received: " + json;
    }
    
    @GetMapping("/request-info")
    public Map<String, Object> getRequestInfo(HttpServletRequest request) {
        Map<String, Object> info = new HashMap<>();
        
        // Basic request information
        info.put("method", request.getMethod());
        info.put("requestURI", request.getRequestURI());
        info.put("queryString", request.getQueryString());
        info.put("remoteAddr", request.getRemoteAddr());
        
        // Headers
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        info.put("headers", headers);
        
        // Security info
        info.put("authType", request.getAuthType());
        info.put("isSecure", request.isSecure());
        
        return info;
    }
    
    @PostMapping(value = "/auth-test", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> testAuth(@RequestBody(required = false) Map<String, Object> payload, 
                                        HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Authentication test endpoint reached successfully");
        
        if (payload != null) {
            response.put("receivedPayload", payload);
        }
        
        // Add request info
        Map<String, Object> requestInfo = new HashMap<>();
        requestInfo.put("method", request.getMethod());
        requestInfo.put("contentType", request.getContentType());
        
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        requestInfo.put("headers", headers);
        
        response.put("requestInfo", requestInfo);
        return response;
    }
    
    @PostMapping("/test-car")
    public Map<String, Object> testCarCreation(@RequestBody Map<String, Object> carData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Log the input
            response.put("received", carData);
            
            // Manual validation
            if (!carData.containsKey("brand") || carData.get("brand") == null) {
                response.put("error", "Brand is missing or null");
                return response;
            }
            if (!carData.containsKey("model") || carData.get("model") == null) {
                response.put("error", "Model is missing or null");
                return response;
            }
            if (!carData.containsKey("year")) {
                response.put("error", "Year is missing");
                return response;
            }
            if (!carData.containsKey("licensePlate") || carData.get("licensePlate") == null) {
                response.put("error", "License plate is missing or null");
                return response;
            }
            if (!carData.containsKey("pricePerDay")) {
                response.put("error", "Price per day is missing");
                return response;
            }
            if (!carData.containsKey("status") || carData.get("status") == null) {
                response.put("error", "Status is missing or null");
                return response;
            }
            
            response.put("validationPassed", true);
            response.put("message", "Car data looks valid");
            
        } catch (Exception e) {
            response.put("error", "Exception: " + e.getMessage());
            e.printStackTrace();
        }
        
        return response;
    }
}
