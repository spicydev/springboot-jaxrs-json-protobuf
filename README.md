# Spring MVC JSON + Protobuf Demo

This project demonstrates a Spring MVC API that serves and accepts both JSON and protobuf payloads on the same endpoint family.

## Stack
- Java 25
- Spring Boot 4
- Spring MVC
- Protocol Buffers (`protobuf-java`)

## API behavior
- `GET /api/test/proto`
  - `Accept: application/json` returns a JSON `Citizen` payload
  - `Accept: application/x-protobuf` returns a protobuf `PersonBinding.Person` payload
- `POST /api/test/proto`
  - `Content-Type: application/json` accepts and returns JSON
  - `Content-Type: application/x-protobuf` accepts and returns protobuf

The controller implementation lives in `src/main/java/com/example/demo/resource/TestController.java`.
