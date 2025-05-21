package com.example.demo.repository;

import com.example.demo.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByClientId(Long clientId);
    List<Rental> findByCarId(Long carId);
    List<Rental> findByStatus(String status);
}
