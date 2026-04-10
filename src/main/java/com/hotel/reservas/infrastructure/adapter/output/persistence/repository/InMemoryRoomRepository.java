package com.hotel.reservas.infrastructure.adapter.output.persistence.repository;

import com.hotel.reservas.domain.model.Room;
import com.hotel.reservas.domain.enums.RoomType;
import com.hotel.reservas.domain.ports.output.RoomRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryRoomRepository implements RoomRepository {
    private final Map<String, Room> storage = new HashMap<>();

    @Override
    public List<Room> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<Room> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Room> findByAvailableAndType(LocalDate startDate, LocalDate endDate, RoomType type) {
        // Simple logic: Room is available if its isAvailable flag is true
        // For a full study case, we'd check overlapping reservations
        return storage.values().stream()
                .filter(room -> room.isAvailable() && (type == null || room.getType() == type))
                .collect(Collectors.toList());
    }

    @Override
    public void save(Room room) {
        storage.put(room.getId(), room);
    }
}
