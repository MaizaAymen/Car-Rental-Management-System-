package com.example.demo.service;

import com.example.demo.dto.CarDto;
import com.example.demo.model.Car;
import com.example.demo.repository.CarRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(CarService.class);

    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream()
                .map(car -> modelMapper.map(car, CarDto.class))
                .collect(Collectors.toList());
    }

    public List<CarDto> getAvailableCars() {
        return carRepository.findByStatus("available").stream()
                .map(car -> modelMapper.map(car, CarDto.class))
                .collect(Collectors.toList());
    }

    public Optional<CarDto> getCarById(Long id) {
        return carRepository.findById(id)
                .map(car -> modelMapper.map(car, CarDto.class));
    }

    public CarDto createCar(CarDto carDto) {
        logger.info("Attempting to create car: {}", carDto);
        if (carDto.getBrand() == null || carDto.getBrand().isEmpty() ||
            carDto.getModel() == null || carDto.getModel().isEmpty() ||
            carDto.getLicensePlate() == null || carDto.getLicensePlate().isEmpty() ||
            carDto.getStatus() == null || carDto.getStatus().isEmpty() ||
            carDto.getYear() == 0 || carDto.getPricePerDay() == 0.0) {
            logger.error("Missing required car fields: {}", carDto);
            throw new RuntimeException("Missing required car fields");
        }
        if (carRepository.existsByLicensePlate(carDto.getLicensePlate())) {
            logger.error("License plate already exists: {}", carDto.getLicensePlate());
            throw new RuntimeException("License plate already exists");
        }
        try {
            Car car = modelMapper.map(carDto, Car.class);
            Car savedCar = carRepository.save(car);
            logger.info("Car created successfully: {}", savedCar);
            return modelMapper.map(savedCar, CarDto.class);
        } catch (Exception e) {
            logger.error("Error creating car: {}", e.getMessage(), e);
            throw new RuntimeException("Error creating car: " + e.getMessage());
        }
    }

    public Optional<CarDto> updateCar(Long id, CarDto carDto) {
        return carRepository.findById(id)
                .map(existingCar -> {
                    // Check if license plate is being changed and if it already exists
                    if (!existingCar.getLicensePlate().equals(carDto.getLicensePlate()) && 
                            carRepository.existsByLicensePlate(carDto.getLicensePlate())) {
                        throw new RuntimeException("License plate already exists");
                    }
                    
                    existingCar.setBrand(carDto.getBrand());
                    existingCar.setModel(carDto.getModel());
                    existingCar.setYear(carDto.getYear());
                    existingCar.setLicensePlate(carDto.getLicensePlate());
                    existingCar.setPricePerDay(carDto.getPricePerDay());
                    existingCar.setStatus(carDto.getStatus());
                    
                    Car updatedCar = carRepository.save(existingCar);
                    return modelMapper.map(updatedCar, CarDto.class);
                });
    }

    public boolean deleteCar(Long id) {
        return carRepository.findById(id)
                .map(car -> {
                    carRepository.delete(car);
                    return true;
                })
                .orElse(false);
    }

    public Optional<CarDto> updateCarStatus(Long id, String status) {
        return carRepository.findById(id)
                .map(existingCar -> {
                    existingCar.setStatus(status);
                    Car updatedCar = carRepository.save(existingCar);
                    return modelMapper.map(updatedCar, CarDto.class);
                });
    }
}
