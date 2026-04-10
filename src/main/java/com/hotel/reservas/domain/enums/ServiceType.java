package com.hotel.reservas.domain.enums;

public enum ServiceType {
    SPA(75.0), 
    RESTAURANT(45.0), 
    TRANSPORT(60.0), 
    BREAKFAST(25.0);

    private final double price;

    ServiceType(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
