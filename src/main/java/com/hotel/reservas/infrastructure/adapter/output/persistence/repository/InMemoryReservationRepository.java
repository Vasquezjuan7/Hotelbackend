package com.hotel.reservas.infrastructure.adapter.output.persistence.repository;

import com.hotel.reservas.domain.model.Reservation;
import com.hotel.reservas.domain.ports.output.ReservationRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryReservationRepository implements ReservationRepository {
    private final Map<String, Reservation> storage = new HashMap<>();

    @Override
    public void save(Reservation reservation) {
        if (reservation.getId() == null) {
            reservation.setId(UUID.randomUUID().toString());
        }
        storage.put(reservation.getId(), reservation);
    }

    @Override
    public Optional<Reservation> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Reservation> findAll() {
        return new ArrayList<>(storage.values());
    }
}
