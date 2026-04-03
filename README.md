# Modular Monolith

This project is a Spring Boot monolith that is intentionally structured as a **modular monolith**: a single deployable application with clear internal boundaries (orders, inventory, payments) and a thin shared layer.

The goal is to be a good starting point for a later migration to microservices, without splitting into services yet.

## Tech

- Java 21
- Spring Boot 4
- Spring MVC + JPA (Hibernate)
- Flyway migrations
- H2 (in-memory) for local/dev
- Actuator endpoints for health/metrics

## Run

```bash
./mvnw spring-boot:run
```

App runs on port `8081`.

## API

Create an order:

```bash
curl -X POST http://localhost:8081/orders ^
  -H "Content-Type: application/json" ^
  -d "{ \"customerName\": \"Alice\", \"totalAmount\": 123.45, \"productId\": 1, \"quantity\": 2, \"paymentStatus\": \"PAID\" }"
```

List orders:

```bash
curl http://localhost:8081/orders
```

## Architecture Notes

- Feature modules live under `com.suprun.demo.modules.*`
  - `orders` orchestrates order creation
  - `inventory` exposes an internal API (`InventoryService`) and owns inventory persistence
  - `payments` exposes internal APIs (`PaymentService`, `PaymentQueryService`) and owns payment persistence
- Controllers use request/response DTOs (no direct exposure of JPA entities).
- DB schema is managed only by Flyway (`src/main/resources/db/migration`).
- A transactional outbox table exists (`outbox_event`) to make eventual messaging straightforward later.
  - Publisher is disabled by default (`app.outbox.publisher.enabled=false`) and is log-only when enabled.

