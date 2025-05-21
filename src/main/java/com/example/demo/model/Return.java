package com.example.demo.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Return {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

    @Column(nullable = false)
    private LocalDate returnDate;

    private String notes;

    private String damages;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Rental getRental() {
        return rental;
    }
    
    public void setRental(Rental rental) {
        this.rental = rental;
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
