# Hotel Reservations Backend

Backend de ejemplo para la gestión de reservas de hotel usando Spring Boot y una arquitectura inspirada en Clean Architecture.

## Resumen

Este proyecto expone un conjunto de servicios REST para:
- crear reservas
- consultar disponibilidad de habitaciones
- obtener reservas y detalle de una reserva
- agregar servicios adicionales a una reserva
- realizar check-in y check-out

La aplicación utiliza repositorios en memoria para habitaciones y reservas, y carga datos iniciales de habitaciones al arranque.

## Tecnologías

- Java 17
- Spring Boot 3.2.4
- Spring Web
- Spring Validation
- Lombok
- Maven

## Arquitectura

El código está organizado en capas:

- `com.hotel.reservas.domain`: modelos, enums y puertos (interfaces) de entrada y salida.
- `com.hotel.reservas.application.service`: la lógica de negocio de hotel implementada en `HotelService`.
- `com.hotel.reservas.infrastructure.adapter.input.rest.controller`: controladores REST.
- `com.hotel.reservas.infrastructure.adapter.output.persistence.repository`: repositorios en memoria.
- `com.hotel.reservas.infrastructure.config`: inicializador de datos.

### Patrón usado

El backend sigue un patrón de puertos y adaptadores:
- `HotelFacade` define la interfaz de operaciones de hotel.
- `HotelService` implementa la lógica y usa repositorios de salida.
- Los controladores REST consumen el facade y exponen API HTTP.

## Modelos principales

- `Reservation`: reserva de habitación con fechas, huésped, habitación, estado, servicios adicionales y llave digital.
- `Room`: representa una habitación con número, tipo, precio base y disponibilidad.
- `Guest`: datos del huésped.
- `Invoice`: factura generada en el check-out.
- `RoomType`, `ReservationStatus`, `Season`, `ServiceType`: enums para tipo de habitación, estado de reserva, temporada y servicios.

## Flujo principal

1. `ReservationController.reserve(...)` recibe una solicitud de reserva.
2. Crea un `Reservation` y llama a `HotelService.createReservation(...)`.
3. `HotelService` valida la habitación, asigna ID, marca estado `PENDING` y determina temporada.
4. La reserva se guarda en `InMemoryReservationRepository`.

Para check-in y check-out:
- `HotelService.checkIn(...)` actualiza el estado a `CHECKED_IN` y genera llave digital.
- `HotelService.checkOut(...)` calcula el total según noches y servicios, cambia a `CHECKED_OUT` y retorna una `Invoice`.

## Repositorios en memoria

- `InMemoryRoomRepository`: guarda habitaciones en un `HashMap` y filtra disponibilidad en función del flag `isAvailable`.
- `InMemoryReservationRepository`: guarda reservas en un `HashMap`.

## Carga inicial de datos

`HotelDataLoader` crea 15 habitaciones en memoria al iniciar la aplicación:
- 5 habitaciones `SINGLE`
- 5 habitaciones `DOUBLE`
- 5 habitaciones `SUITE`

## Endpoints disponibles

Base URL: `/api/hotel`

- `POST /reservar`
  - Crea una nueva reserva.
  - Request DTO: `ReservationRequestDTO`.

- `GET /disponibilidad`
  - Consulta habitaciones disponibles entre dos fechas, opcionalmente filtradas por tipo.
  - Parámetros: `startDate`, `endDate`, `type`.

- `GET /reservas`
  - Devuelve todas las reservas.

- `GET /reserva/{reservaid}`
  - Devuelve una reserva por ID.

- `POST /servicios/{reservaid}`
  - Agrega un servicio adicional a una reserva.
  - Request DTO: `ServiceRequestDTO`.

- `PUT /checkin/{reservaid}`
  - Realiza el check-in y asigna llave digital.

- `PUT /checkout/{reservaid}`
  - Realiza el check-out y devuelve la factura final.

## Ejecutar la aplicación

Desde la raíz del proyecto:

```bash
mvn spring-boot:run
```

O construir y ejecutar el JAR:

```bash
mvn clean package
java -jar target/reservas-0.0.1-SNAPSHOT.jar
```

## Notas importantes

- La persistencia es en memoria, por lo que los datos se pierden al reiniciar la aplicación.
- La lógica de disponibilidad de habitaciones es básica y solo verifica la bandera `isAvailable`.
- Para un despliegue real, se recomienda reemplazar los repositorios en memoria por una base de datos.

## Contacto

Este README fue generado automaticamente para describir el funcionamiento del backend y sus principales componentes.
