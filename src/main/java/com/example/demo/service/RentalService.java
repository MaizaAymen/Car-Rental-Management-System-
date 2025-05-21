package com.example.demo.service;

import com.example.demo.dto.RentalDto;
import com.example.demo.model.Car;
import com.example.demo.model.Client;
import com.example.demo.model.Rental;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.RentalRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarService carService;

    @Autowired
    private ModelMapper modelMapper;

    public List<RentalDto> getAllRentals() {
        return rentalRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<RentalDto> getRentalsByClientId(Long clientId) {
        return rentalRepository.findByClientId(clientId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<RentalDto> getRentalById(Long id) {
        return rentalRepository.findById(id)
                .map(this::convertToDto);
    }

    @Transactional
    public RentalDto createRental(RentalDto rentalDto) {
        // Validate client exists
        Client client = clientRepository.findById(rentalDto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // Validate car exists and is available
        Car car = carRepository.findById(rentalDto.getCarId())
                .orElseThrow(() -> new RuntimeException("Car not found"));

        if (!"available".equals(car.getStatus())) {
            throw new RuntimeException("Car is not available for rental");
        }

        // Calculate total price
        long days = ChronoUnit.DAYS.between(rentalDto.getStartDate(), rentalDto.getEndDate());
        if (days < 1) {
            throw new RuntimeException("End date must be after start date");
        }
        double totalPrice = days * car.getPricePerDay();

        // Create and save the rental
        Rental rental = new Rental();
        rental.setClient(client);
        rental.setCar(car);
        rental.setStartDate(rentalDto.getStartDate());
        rental.setEndDate(rentalDto.getEndDate());
        rental.setTotalPrice(totalPrice);
        rental.setStatus("active");

        // Update car status to rented
        car.setStatus("rented");
        carRepository.save(car);

        Rental savedRental = rentalRepository.save(rental);
        return convertToDto(savedRental);
    }

    @Transactional
    public Optional<RentalDto> updateRental(Long id, RentalDto rentalDto) {
        return rentalRepository.findById(id)
                .map(existingRental -> {
                    // Update fields
                    if (rentalDto.getStatus() != null) {
                        existingRental.setStatus(rentalDto.getStatus());
                    }
                    
                    if (rentalDto.getStartDate() != null && rentalDto.getEndDate() != null) {
                        existingRental.setStartDate(rentalDto.getStartDate());
                        existingRental.setEndDate(rentalDto.getEndDate());
                        
                        // Recalculate total price
                        long days = ChronoUnit.DAYS.between(rentalDto.getStartDate(), rentalDto.getEndDate());
                        if (days < 1) {
                            throw new RuntimeException("End date must be after start date");
                        }
                        double totalPrice = days * existingRental.getCar().getPricePerDay();
                        existingRental.setTotalPrice(totalPrice);
                    }
                    
                    // If status is "completed", update car status to available
                    if ("completed".equals(rentalDto.getStatus())) {
                        Car car = existingRental.getCar();
                        car.setStatus("available");
                        carRepository.save(car);
                    }
                    
                    Rental updatedRental = rentalRepository.save(existingRental);
                    return convertToDto(updatedRental);
                });
    }

    @Transactional
    public boolean deleteRental(Long id) {
        return rentalRepository.findById(id)
                .map(rental -> {
                    // If the rental is active, make the car available again
                    if ("active".equals(rental.getStatus())) {
                        Car car = rental.getCar();
                        car.setStatus("available");
                        carRepository.save(car);
                    }
                    
                    rentalRepository.delete(rental);
                    return true;
                })
                .orElse(false);
    }

    private RentalDto convertToDto(Rental rental) {
        RentalDto rentalDto = modelMapper.map(rental, RentalDto.class);
        rentalDto.setClientId(rental.getClient().getId());
        rentalDto.setCarId(rental.getCar().getId());
        return rentalDto;
    }
}
