package com.example.demo.controller;

import com.example.demo.dto.ReturnDto;
import com.example.demo.service.ReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/returns")
public class ReturnController {

    @Autowired
    private ReturnService returnService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<List<ReturnDto>> getAllReturns() {
        return ResponseEntity.ok(returnService.getAllReturns());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<ReturnDto> getReturnById(@PathVariable Long id) {
        return returnService.getReturnById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/rental/{rentalId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<ReturnDto> getReturnByRentalId(@PathVariable Long rentalId) {
        return returnService.getReturnByRentalId(rentalId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<?> createReturn(@RequestBody ReturnDto returnDto) {
        try {
            ReturnDto createdReturn = returnService.createReturn(returnDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReturn);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Creation failed: " + e.getMessage());
        }
    }
}
