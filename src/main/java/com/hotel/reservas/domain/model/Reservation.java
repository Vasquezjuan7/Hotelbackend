package com.hotel.reservas.domain.model;

import com.hotel.reservas.domain.enums.ReservationStatus;
import com.hotel.reservas.domain.enums.Season;
import com.hotel.reservas.domain.enums.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation {
    private String id;
    private Guest guest;
    private Room room;
    private LocalDate startDate;
    private LocalDate endDate;
    private Season season;
    @Builder.Default
    private List<ServiceType> additionalServices = new ArrayList<>();
    private ReservationStatus status;
    private String digitalKey;

    public void addService(ServiceType service) {
        if (additionalServices == null) {
            additionalServices = new ArrayList<>();
        }
        additionalServices.add(service);
    }
}
