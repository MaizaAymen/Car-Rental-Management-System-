package com.example.demo.repository;

import com.example.demo.model.Return;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReturnRepository extends JpaRepository<Return, Long> {
    Optional<Return> findByRentalId(Long rentalId);
}
