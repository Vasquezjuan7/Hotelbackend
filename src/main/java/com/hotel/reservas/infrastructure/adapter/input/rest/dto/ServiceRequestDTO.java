package com.hotel.reservas.infrastructure.adapter.input.rest.dto;

import com.hotel.reservas.domain.enums.ServiceType;
import lombok.Data;

@Data
public class ServiceRequestDTO {
    private ServiceType serviceType;
}
