package com.hotel.reservas.infrastructure.adapter.input.rest.controller;

import com.hotel.reservas.domain.model.Room;
import com.hotel.reservas.domain.ports.output.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotel")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RoomController {

    private final RoomRepository roomRepository;

    /**
     * GET /api/hotel/habitaciones
     * Lista todas las habitaciones del hotel
     */
    @GetMapping("/habitaciones")
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomRepository.findAll());
    }

    /**
     * GET /api/hotel/habitaciones/{id}
     * Obtiene una habitación por ID
     */
    @GetMapping("/habitaciones/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable String id) {
        return roomRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
