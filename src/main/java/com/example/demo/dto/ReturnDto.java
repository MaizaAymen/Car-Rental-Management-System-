package com.example.demo.dto;

import java.time.LocalDate;

public class ReturnDto {
    private Long id;
    private Long rentalId;
    private LocalDate returnDate;
    private String notes;
    private String damages;
    
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
    
    public LocalDate getReturnDate() {
        return returnDate;
    }
    
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public String getDamages() {
        return damages;
    }
    
    public void setDamages(String damages) {
        this.damages = damages;
    }
}
