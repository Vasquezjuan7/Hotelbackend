package com.hotel.reservas.infrastructure.adapter.input.rest.controller;

import com.hotel.reservas.domain.enums.RoomType;
import com.hotel.reservas.domain.enums.ServiceType;
import com.hotel.reservas.domain.model.Guest;
import com.hotel.reservas.domain.model.Invoice;
import com.hotel.reservas.domain.model.Reservation;
import com.hotel.reservas.domain.model.Room;
import com.hotel.reservas.domain.ports.input.HotelFacade;
import com.hotel.reservas.domain.ports.output.ReservationRepository;
import com.hotel.reservas.infrastructure.adapter.input.rest.dto.ReservationRequestDTO;
import com.hotel.reservas.infrastructure.adapter.input.rest.dto.ServiceRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/hotel")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReservationController {

    private final HotelFacade hotelFacade;
    private final ReservationRepository reservationRepository;

    /**
     * POST /api/hotel/reservar
     * Creates a new reservation
     */
    @PostMapping("/reservar")
    public ResponseEntity<Reservation> reserve(@RequestBody ReservationRequestDTO request) {
        Reservation reservation = Reservation.builder()
                .guest(Guest.builder()
                        .name(request.getGuestName())
                        .email(request.getGuestEmail())
                        .phone(request.getGuestPhone())
                        .build())
                .room(Room.builder().id(request.getRoomId()).build())
                .startDate(LocalDate.parse(request.getStartDate()))
                .endDate(LocalDate.parse(request.getEndDate()))
                .build();
        
        return ResponseEntity.ok(hotelFacade.createReservation(reservation));
    }

    /**
     * GET /api/hotel/disponibilidad
     * Returns available rooms filtered by date and type
     */
    @GetMapping("/disponibilidad")
    public ResponseEntity<List<Room>> getAvailability(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) RoomType type) {
        
        return ResponseEntity.ok(hotelFacade.getAvailability(startDate, endDate, type));
    }

    /**
     * GET /api/hotel/reservas
     * Lists all reservations (for management lookup)
     */
    @GetMapping("/reservas")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationRepository.findAll());
    }

    /**
     * GET /api/hotel/reserva/{reservaid}
     * Gets a specific reservation by ID
     */
    @GetMapping("/reserva/{reservaid}")
    public ResponseEntity<Reservation> getReservation(@PathVariable String reservaid) {
        return ResponseEntity.ok(hotelFacade.getReservation(reservaid));
    }

    /**
     * POST /api/hotel/servicios/{reservaid}
     * Adds a service to an existing reservation
     */
    @PostMapping("/servicios/{reservaid}")
    public ResponseEntity<Void> addService(
            @PathVariable String reservaid,
            @RequestBody ServiceRequestDTO request) {
        
        hotelFacade.addService(reservaid, request.getServiceType());
        return ResponseEntity.ok().build();
    }

    /**
     * PUT /api/hotel/checkin/{reservaid}
     * Performs check-in: sets status to CHECKED_IN and generates digital key
     */
    @PutMapping("/checkin/{reservaid}")
    public ResponseEntity<Reservation> checkIn(@PathVariable String reservaid) {
        hotelFacade.checkIn(reservaid);
        return ResponseEntity.ok(hotelFacade.getReservation(reservaid));
    }

    /**
     * PUT /api/hotel/checkout/{reservaid}
     * Performs check-out and returns the final invoice
     */
    @PutMapping("/checkout/{reservaid}")
    public ResponseEntity<Invoice> checkOut(@PathVariable String reservaid) {
        return ResponseEntity.ok(hotelFacade.checkOut(reservaid));
    }
}
