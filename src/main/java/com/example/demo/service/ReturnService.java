package com.example.demo.service;

import com.example.demo.dto.ReturnDto;
import com.example.demo.model.Car;
import com.example.demo.model.Rental;
import com.example.demo.model.Return;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.RentalRepository;
import com.example.demo.repository.ReturnRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReturnService {

    @Autowired
    private ReturnRepository returnRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<ReturnDto> getAllReturns() {
        return returnRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<ReturnDto> getReturnById(Long id) {
        return returnRepository.findById(id)
                .map(this::convertToDto);
    }

    public Optional<ReturnDto> getReturnByRentalId(Long rentalId) {
        return returnRepository.findByRentalId(rentalId)
                .map(this::convertToDto);
    }

    @Transactional
    public ReturnDto createReturn(ReturnDto returnDto) {
        // Validate rental exists and is active
        Rental rental = rentalRepository.findById(returnDto.getRentalId())
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        if (!"active".equals(rental.getStatus())) {
            throw new RuntimeException("Rental is not active");
        }

        // Check if return already exists for this rental
        if (returnRepository.findByRentalId(rental.getId()).isPresent()) {
            throw new RuntimeException("Return record already exists for this rental");
        }

        // Update car status to available
        Car car = rental.getCar();
        car.setStatus("available");
        carRepository.save(car);

        // Update rental status to completed
        rental.setStatus("completed");
        rentalRepository.save(rental);

        // Create return record
        Return returnRecord = new Return();
        returnRecord.setRental(rental);
        returnRecord.setReturnDate(returnDto.getReturnDate() != null ? returnDto.getReturnDate() : LocalDate.now());
        returnRecord.setNotes(returnDto.getNotes());
        returnRecord.setDamages(returnDto.getDamages());

        Return savedReturn = returnRepository.save(returnRecord);
        return convertToDto(savedReturn);
    }

    private ReturnDto convertToDto(Return returnRecord) {
        ReturnDto returnDto = modelMapper.map(returnRecord, ReturnDto.class);
        returnDto.setRentalId(returnRecord.getRental().getId());
        return returnDto;
    }
}
