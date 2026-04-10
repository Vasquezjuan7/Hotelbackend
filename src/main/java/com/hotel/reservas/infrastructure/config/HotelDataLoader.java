package com.hotel.reservas.infrastructure.config;

import com.hotel.reservas.domain.model.Room;
import com.hotel.reservas.domain.enums.RoomType;
import com.hotel.reservas.domain.ports.output.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class HotelDataLoader implements CommandLineRunner {

    private final RoomRepository roomRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize rooms with Elite pricing
        for (int i = 1; i <= 5; i++) {
            roomRepository.save(createRoom("V10" + i, RoomType.SINGLE, 150.0));
        }
        for (int i = 1; i <= 5; i++) {
            roomRepository.save(createRoom("V20" + i, RoomType.DOUBLE, 280.0));
        }
        for (int i = 1; i <= 5; i++) {
            roomRepository.save(createRoom("V30" + i, RoomType.SUITE, 450.0));
        }
        System.out.println("Initialized Elite rooms at LuxeRest Estate.");
    }

    private Room createRoom(String number, RoomType type, double price) {
        return Room.builder()
                .id(UUID.randomUUID().toString())
                .number(number)
                .type(type)
                .basePrice(price)
                .isAvailable(true)
                .build();
    }
}
