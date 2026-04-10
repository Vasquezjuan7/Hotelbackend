package com.hotel.reservas.domain.ports.output;

import com.hotel.reservas.domain.model.Reservation;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    void save(Reservation reservation);
    Optional<Reservation> findById(String id);
    List<Reservation> findAll();
}
