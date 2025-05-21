package com.example.demo.controller;

import com.example.demo.dto.ClientDto;
import com.example.demo.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<List<ClientDto>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long id) {
        return clientService.getClientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody ClientDto clientDto) {
        try {
            ClientDto createdClient = clientService.createClient(clientDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Creation failed: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public ResponseEntity<?> updateClient(@PathVariable Long id, @RequestBody ClientDto clientDto) {
        try {
            return clientService.updateClient(id, clientDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Update failed: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        return clientService.deleteClient(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}
