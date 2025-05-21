package com.example.demo.service;

import com.example.demo.dto.ClientDto;
import com.example.demo.model.Client;
import com.example.demo.repository.ClientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<ClientDto> getAllClients() {
        return clientRepository.findAll().stream()
                .map(client -> modelMapper.map(client, ClientDto.class))
                .collect(Collectors.toList());
    }

    public Optional<ClientDto> getClientById(Long id) {
        return clientRepository.findById(id)
                .map(client -> modelMapper.map(client, ClientDto.class));
    }

    public ClientDto createClient(ClientDto clientDto) {
        if (clientRepository.existsByEmail(clientDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (clientRepository.existsByPhone(clientDto.getPhone())) {
            throw new RuntimeException("Phone number already exists");
        }

        Client client = modelMapper.map(clientDto, Client.class);
        Client savedClient = clientRepository.save(client);
        return modelMapper.map(savedClient, ClientDto.class);
    }

    public Optional<ClientDto> updateClient(Long id, ClientDto clientDto) {
        return clientRepository.findById(id)
                .map(existingClient -> {
                    // Check if email is being changed and if it already exists
                    if (!existingClient.getEmail().equals(clientDto.getEmail()) && 
                            clientRepository.existsByEmail(clientDto.getEmail())) {
                        throw new RuntimeException("Email already exists");
                    }
                    
                    // Check if phone is being changed and if it already exists
                    if (!existingClient.getPhone().equals(clientDto.getPhone()) && 
                            clientRepository.existsByPhone(clientDto.getPhone())) {
                        throw new RuntimeException("Phone number already exists");
                    }
                    
                    existingClient.setName(clientDto.getName());
                    existingClient.setEmail(clientDto.getEmail());
                    existingClient.setPhone(clientDto.getPhone());
                    existingClient.setAddress(clientDto.getAddress());
                    
                    Client updatedClient = clientRepository.save(existingClient);
                    return modelMapper.map(updatedClient, ClientDto.class);
                });
    }

    public boolean deleteClient(Long id) {
        return clientRepository.findById(id)
                .map(client -> {
                    clientRepository.delete(client);
                    return true;
                })
                .orElse(false);
    }
}
