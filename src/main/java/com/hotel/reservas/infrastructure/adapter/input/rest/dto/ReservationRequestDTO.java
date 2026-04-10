package com.hotel.reservas.infrastructure.adapter.input.rest.dto;

import com.hotel.reservas.domain.enums.RoomType;
import lombok.Data;

@Data
public class ReservationRequestDTO {
    private String guestName;
    private String guestEmail;
    private String guestPhone;
    private String roomId;
    private String startDate;
    private String endDate;
}
