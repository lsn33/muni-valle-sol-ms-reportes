# MS-Reportes — Municipalidad Valle del Sol

Microservicio de gestión de reportes de incendios con coordenadas GPS para la plataforma de la Municipalidad Valle del Sol.

## Tecnologías
- Java 21
- Spring Boot 4.0.6
- Spring Data JPA + PostgreSQL
- Flyway (migraciones de BD)
- Maven


## Patrones de diseño implementados
- **Repository Pattern**: `ReporteRepository` desacopla el acceso a datos de la lógica de negocio
- **Factory Method**: `ReporteFactory` crea distintos tipos de reporte (INCENDIO, HUMO, SOSPECHOSO) con lógica de prioridad


## Requisitos
- Java 21
- Maven
- Cuenta en Neon.tech (PostgreSQL en la nube)


## Configuración
Crea el archivo `src/main/resources/application.yml` con tus credenciales:

```yaml
spring:
  application:
    name: ms-reportes
  datasource:
    url: jdbc:postgresql://<host>/ms-reportes?sslmode=require&channelBinding=require
    username: <usuario>
    password: <password>
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true

server:
  port: 8082
```


## Ejecutar el proyecto
```bash
./mvnw spring-boot:run
```

El servidor arranca en `http://localhost:8082`


## Endpoints

### Crear reporte
POST /api/reportes
Content-Type: application/json
{
"titulo": "Incendio cerro San Cristobal",
"descripcion": "Humo visible desde lejos",
"latitud": -33.4489,
"longitud": -70.6693,
"tipo": "INCENDIO",
"emailUsuario": "lucas@test.com"
}

Tipos disponibles: `INCENDIO`, `HUMO`, `SOSPECHOSO`


### Listar todos los reportes
GET /api/reportes


### Listar reportes activos
GET /api/reportes/activos


### Buscar reporte por ID
GET /api/reportes/{id}


### Actualizar estado de reporte
PUT /api/reportes/{id}/estado
Content-Type: application/json
{
"estado": "EN_ATENCION"
}


## Migraciones de BD (Flyway)

| Versión | Archivo | Descripción |
|---------|---------|-------------|
| V1 | V1__crear_tabla_reporte.sql | Crea tabla reporte con campos id, titulo, descripcion, latitud, longitud, tipo, estado, email_usuario, fecha_creacion |

## Estrategia de Branching

Se utiliza Git Flow:
- `main` → código estable y probado
- `develop` → integración de features
- `feature/*` → desarrollo de funcionalidades


## Estructura del proyecto
src/main/java/cl/municipalidad/ms_reportes/
├── reporte/
│   ├── Reporte.java
│   ├── ReporteDTO.java
│   ├── ReporteRepository.java
│   ├── ReporteService.java
│   ├── ReporteController.java
│   └── factory/
│       ├── TipoReporte.java
│       └── ReporteFactory.java
└── MsReportesApplication.java