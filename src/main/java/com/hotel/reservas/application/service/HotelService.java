package com.hotel.reservas.application.service;

import com.hotel.reservas.domain.enums.ReservationStatus;
import com.hotel.reservas.domain.enums.Season;
import com.hotel.reservas.domain.enums.ServiceType;
import com.hotel.reservas.domain.model.Invoice;
import com.hotel.reservas.domain.model.Reservation;
import com.hotel.reservas.domain.model.Room;
import com.hotel.reservas.domain.enums.RoomType;
import com.hotel.reservas.domain.ports.input.HotelFacade;
import com.hotel.reservas.domain.ports.output.ReservationRepository;
import com.hotel.reservas.domain.ports.output.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HotelService implements HotelFacade {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public Reservation createReservation(Reservation reservation) {
        // Business Rule: Validate room existence
        Room room = roomRepository.findById(reservation.getRoom().getId())
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada: " + reservation.getRoom().getId()));
        
        reservation.setId(UUID.randomUUID().toString()); // Generate ID before save
        reservation.setRoom(room);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setSeason(determineSeason(reservation.getStartDate()));
        
        reservationRepository.save(reservation);
        return reservation;
    }

    @Override
    public void addService(String reservationId, ServiceType serviceType) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        
        reservation.addService(serviceType);
        reservationRepository.save(reservation);
    }

    @Override
    public void checkIn(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        
        reservation.setStatus(ReservationStatus.CHECKED_IN);
        reservation.setDigitalKey(UUID.randomUUID().toString());
        reservationRepository.save(reservation);
    }

    @Override
    public Invoice checkOut(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.setStatus(ReservationStatus.CHECKED_OUT);
        
        // Calculate Charges
        long nights = ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
        if (nights <= 0) nights = 1;

        double roomRate = reservation.getRoom().getBasePrice() * reservation.getSeason().getMultiplier();
        double totalRoomCharges = roomRate * nights;
        
        double totalServiceCharges = reservation.getAdditionalServices().stream()
                .mapToDouble(ServiceType::getPrice)
                .sum();
        
        double total = totalRoomCharges + totalServiceCharges;

        List<String> details = new ArrayList<>();
        details.add("Suite " + reservation.getRoom().getNumber() + " — " + reservation.getRoom().getType());
        details.add("Noches: " + nights);
        details.add("Temporada: " + (reservation.getSeason() == Season.HIGH ? "Alta (x1.5)" : "Baja (x1.0)"));
        details.add("Tarifa base por noche: $" + reservation.getRoom().getBasePrice());
        details.add("Tarifa aplicada por noche: $" + String.format("%.2f", roomRate));
        details.add("Total habitación (" + nights + " noche(s)): $" + String.format("%.2f", totalRoomCharges));
        
        // Add service breakdown
        for (ServiceType st : reservation.getAdditionalServices()) {
            details.add("Servicio — " + st.name() + ": $" + st.getPrice());
        }
        
        Invoice invoice = Invoice.builder()
                .id(UUID.randomUUID().toString())
                .reservationId(reservationId)
                .guest(reservation.getGuest())
                .roomNumber(reservation.getRoom().getNumber())
                .roomCharges(totalRoomCharges)
                .serviceCharges(totalServiceCharges)
                .totalAmount(total)
                .details(details)
                .build();
        
        reservationRepository.save(reservation);
        return invoice;
    }

    @Override
    public List<Room> getAvailability(LocalDate startDate, LocalDate endDate, RoomType type) {
        return roomRepository.findByAvailableAndType(startDate, endDate, type);
    }

    @Override
    public Reservation getReservation(String id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
    }

    private Season determineSeason(LocalDate date) {
        // Simple logic for study case: Dec-Jan and Jun-Jul are high season
        int month = date.getMonthValue();
        if (month == 12 || month == 1 || month == 6 || month == 7) {
            return Season.HIGH;
        }
        return Season.LOW;
    }
}
