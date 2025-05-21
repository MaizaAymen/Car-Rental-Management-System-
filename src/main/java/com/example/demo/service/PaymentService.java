package com.example.demo.service;

import com.example.demo.dto.PaymentDto;
import com.example.demo.model.Payment;
import com.example.demo.model.Rental;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.RentalRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<PaymentDto> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PaymentDto> getPaymentsByRentalId(Long rentalId) {
        return paymentRepository.findByRentalId(rentalId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<PaymentDto> getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .map(this::convertToDto);
    }

    public PaymentDto createPayment(PaymentDto paymentDto) {
        // Validate rental exists
        Rental rental = rentalRepository.findById(paymentDto.getRentalId())
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        Payment payment = new Payment();
        payment.setRental(rental);
        payment.setAmount(paymentDto.getAmount());
        payment.setPaymentDate(paymentDto.getPaymentDate() != null ? paymentDto.getPaymentDate() : LocalDate.now());
        payment.setMethod(paymentDto.getMethod());

        Payment savedPayment = paymentRepository.save(payment);
        return convertToDto(savedPayment);
    }

    private PaymentDto convertToDto(Payment payment) {
        PaymentDto paymentDto = modelMapper.map(payment, PaymentDto.class);
        paymentDto.setRentalId(payment.getRental().getId());
        return paymentDto;
    }
}
