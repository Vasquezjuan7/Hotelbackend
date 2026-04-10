package com.hotel.reservas.domain.ports.output;

import com.hotel.reservas.domain.model.Room;
import com.hotel.reservas.domain.enums.RoomType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    List<Room> findAll();
    Optional<Room> findById(String id);
    List<Room> findByAvailableAndType(LocalDate startDate, LocalDate endDate, RoomType type);
    void save(Room room);
}
