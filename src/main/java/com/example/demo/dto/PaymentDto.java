package com.example.demo.dto;

import java.time.LocalDate;

public class PaymentDto {
    private Long id;
    private Long rentalId;
    private double amount;
    private LocalDate paymentDate;
    private String method;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getRentalId() {
        return rentalId;
    }
    
    public void setRentalId(Long rentalId) {
        this.rentalId = rentalId;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public LocalDate getPaymentDate() {
        return paymentDate;
    }
    
    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public String getMethod() {
        return method;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }
}
