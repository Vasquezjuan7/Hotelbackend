package com.hotel.reservas.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Invoice {
    private String id;
    private String reservationId;
    private Guest guest;
    private String roomNumber;
    private double roomCharges;
    private double serviceCharges;
    private double totalAmount;
    private List<String> details;
}
