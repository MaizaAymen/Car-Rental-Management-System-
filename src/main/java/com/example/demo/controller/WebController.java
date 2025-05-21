package com.example.demo.controller;

import com.example.demo.dto.UserCreateDto;
import com.example.demo.service.CarService;
import com.example.demo.service.ClientService;
import com.example.demo.service.RentalService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web")
public class WebController {    @Autowired
    private CarService carService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private RentalService rentalService;
    
    @Autowired
    private UserService userService;

    @GetMapping
    public String home(Model model) {
        model.addAttribute("cars", carService.getAllCars());
        model.addAttribute("availableCars", carService.getAvailableCars());
        return "home";
    }

    @GetMapping("/cars")
    public String cars(Model model) {
        model.addAttribute("cars", carService.getAllCars());
        return "cars";
    }

    @GetMapping("/clients")
    public String clients(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        return "clients";
    }    @GetMapping("/rentals")
    public String rentals(Model model) {
        model.addAttribute("rentals", rentalService.getAllRentals());
        return "rentals";
    }
    
    @GetMapping("/rentals/{id}")
    public String viewRental(@org.springframework.web.bind.annotation.PathVariable Long id, Model model) {
        model.addAttribute("rental", rentalService.getRentalById(id).orElse(null));
        return "rental-view";
    }
    
    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UserCreateDto());
        return "register";
    }
}
