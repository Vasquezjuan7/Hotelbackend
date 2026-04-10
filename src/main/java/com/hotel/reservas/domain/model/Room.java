package com.hotel.reservas.domain.model;

import com.hotel.reservas.domain.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Room {
    private String id;
    private String number;
    private RoomType type;
    private double basePrice;
    private boolean isAvailable;
}
