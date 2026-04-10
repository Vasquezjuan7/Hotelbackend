package com.hotel.reservas.domain.ports.input;

import com.hotel.reservas.domain.model.Invoice;
import com.hotel.reservas.domain.model.Reservation;
import com.hotel.reservas.domain.model.Room;
import com.hotel.reservas.domain.enums.RoomType;
import com.hotel.reservas.domain.enums.ServiceType;

import java.time.LocalDate;
import java.util.List;

public interface HotelFacade {
    Reservation createReservation(Reservation reservation);
    void addService(String reservationId, ServiceType serviceType);
    void checkIn(String reservationId);
    Invoice checkOut(String reservationId);
    List<Room> getAvailability(LocalDate startDate, LocalDate endDate, RoomType type);
    Reservation getReservation(String id);
}
