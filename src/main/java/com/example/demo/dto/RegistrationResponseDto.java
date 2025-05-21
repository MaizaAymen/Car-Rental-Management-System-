package com.example.demo.dto;

public class RegistrationResponseDto {
    private UserDto user;
    private String message;
    private boolean success;
    
    public RegistrationResponseDto() {
    }
    
    public RegistrationResponseDto(UserDto user) {
        this.user = user;
        this.success = true;
        this.message = "Registration successful";
    }
    
    public RegistrationResponseDto(String errorMessage) {
        this.success = false;
        this.message = errorMessage;
    }
    
    public UserDto getUser() {
        return user;
    }
    
    public void setUser(UserDto user) {
        this.user = user;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
