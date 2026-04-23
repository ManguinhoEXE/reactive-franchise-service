# Reactive API - Prueba Tecnica

API REST reactiva para gestion de franquicias, sucursales y productos, construida con Spring Boot y WebFlux sobre PostgreSQL (Supabase) via R2DBC.

## Arquitectura Del Proyecto

### Stack tecnologico
- Spring Boot `4.0.5`
- Spring WebFlux (programacion reactiva con `Mono` y `Flux`)
- Spring Data R2DBC
- PostgreSQL (Supabase)
- OpenAPI/Swagger (`springdoc-openapi-starter-webflux-ui 3.0.3`)
- Java `26`
- Maven `3.9+`

### Capas (clean layering)
- `presentation`: controladores HTTP y contrato REST.
- `application`: casos de uso, DTOs y servicios de aplicacion.
- `domain`: modelos de dominio.
- `infrastructure`: persistencia, mapeadores y manejo global de errores.

### Nombres clave (terminologia nativa)
- `Mono<T>`: flujo reactivo de 0..1 elemento.
- `Flux<T>`: flujo reactivo de 0..N elementos.
- `Backpressure`: control de demanda para evitar saturacion.
- `Non-blocking I/O`: operaciones asincronas sin bloqueo de hilos.

## Inicio Rapido (Supabase + Maven)

### 1) Configurar variables de entorno
Copia el archivo de ejemplo y completa los valores reales:

```bash
cp .env.example .env
```

Contenido esperado en `.env`:

```env
DB_URL=r2dbc:postgresql://db.YOUR_PROJECT_REF.supabase.co:5432/postgres?sslMode=require
DB_USERNAME=postgres
DB_PASSWORD=YOUR_SUPABASE_DB_PASSWORD
DB_SSL_MODE=require
```

### 2) Compilar y ejecutar local

Compilar:

```bash
mvn clean package
```

Ejecutar con variables cargadas (PowerShell):

```powershell
Get-Content .env | ForEach-Object { if ($_ -match '^[\s]*#' -or $_ -match '^[\s]*$') { return }; $pair = $_ -split '=',2; if ($pair.Length -eq 2) { [System.Environment]::SetEnvironmentVariable($pair[0].Trim(), $pair[1].Trim(), 'Process') } }
mvn spring-boot:run
```

La API queda disponible en:
- `http://localhost:8080`

## Ejecucion Con Docker

### 1) Construir imagen

```bash
docker build -t reactive-api:latest .
```

### 2) Ejecutar contenedor

```bash
docker run --rm -p 8080:8080 --env-file .env reactive-api:latest
```

### 3) Verificar salud basica

```bash
curl http://localhost:8080/v3/api-docs
```

## Documentacion De La API (Swagger/OpenAPI)

Con la aplicacion corriendo:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

La especificacion OpenAPI se configura en:
- `src/main/java/com/pruebadev/reactiveapi/infrastructure/config/OpenApiConfig.java`

## Endpoints Principales

### Franquicias
- `POST /api/franquicias`
- `GET /api/franquicias`
- `GET /api/franquicias/{id}`
- `PUT /api/franquicias/{id}`

### Sucursales
- `POST /api/sucursales`
- `GET /api/sucursales`
- `GET /api/sucursales/{id}`
- `GET /api/sucursales/franquicia/{franquiciaId}`
- `PUT /api/sucursales/{id}`

### Productos
- `POST /api/productos`
- `GET /api/productos`
- `GET /api/productos/{id}`
- `GET /api/productos/sucursal/{sucursalId}`
- `PATCH /api/productos/{id}/stock`
- `PUT /api/productos/{id}`
- `DELETE /api/productos/{id}`
- `GET /api/productos/franquicia/{franquiciaId}/max-stock`

## Ventajas Tecnicas De La Programacion Reactiva En Nube

El modelo reactivo de WebFlux permite maximizar throughput con un numero menor de hilos gracias a I/O no bloqueante y composicion asincrona de flujos. En entornos cloud esto mejora la eficiencia de CPU/memoria bajo alta concurrencia, reduce tiempos de espera cuando hay latencia de red/DB y facilita escalar horizontalmente con menor costo por solicitud.

## Pruebas

Ejecutar pruebas unitarias y de capa web:

```bash
mvn test
```

Reportes de pruebas:
- `target/surefire-reports/`
