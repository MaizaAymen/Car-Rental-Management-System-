package com.example.demo.controller;

import com.example.demo.dto.RentalDto;
import com.example.demo.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<List<RentalDto>> getAllRentals() {
        return ResponseEntity.ok(rentalService.getAllRentals());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<RentalDto> getRentalById(@PathVariable Long id) {
        return rentalService.getRentalById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<List<RentalDto>> getRentalsByClientId(@PathVariable Long clientId) {
        return ResponseEntity.ok(rentalService.getRentalsByClientId(clientId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<?> createRental(@RequestBody RentalDto rentalDto) {
        try {
            RentalDto createdRental = rentalService.createRental(rentalDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRental);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Creation failed: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<?> updateRental(@PathVariable Long id, @RequestBody RentalDto rentalDto) {
        try {
            return rentalService.updateRental(id, rentalDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Update failed: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteRental(@PathVariable Long id) {
        return rentalService.deleteRental(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}
