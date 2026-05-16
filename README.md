# MS-Reportes — Municipalidad Valle del Sol
Microservicio de gestión de reportes de incendios con coordenadas GPS para la plataforma de la Municipalidad Valle del Sol.


## Tecnologías
- Java 25
- Spring Boot 4.0.6
- Spring Data JPA + PostgreSQL
- Flyway (migraciones de BD)
- Maven


## Patrones de diseño implementados

### 1. Repository Pattern
`ReportRepository` define una interfaz que extiende `JpaRepository`, desacoplando el acceso a datos de la lógica de negocio. Permite realizar operaciones CRUD sobre la entidad `Report` sin exponer detalles de implementación.

### 2. Factory Method
`ReportFactory` implementa el patrón Factory Method para crear distintos tipos de reporte según su naturaleza. Cada tipo tiene comportamiento distinto: INCENDIO se crea con prioridad ALTA y estado ACTIVO, HUMO con prioridad MEDIA y estado EN_REVISION, y SOSPECHOSO con prioridad BAJA y estado PENDIENTE.


## Casos de uso
| Caso de uso | Descripción |
|-------------|-------------|
| Crear reporte | Un ciudadano reporta un incendio, humo o situación sospechosa con coordenadas GPS |
| Listar reportes activos | El sistema lista todos los reportes con estado ACTIVO para el mapa |
| Buscar reporte por ID | El sistema retorna un reporte específico por su identificador |
| Actualizar estado | Un funcionario actualiza el estado del reporte (ACTIVO, EN_ATENCION, RESUELTO) |
| Eliminar reporte | Un funcionario elimina un reporte del sistema |


## Requisitos
- Java 25
- Maven
- Cuenta en Neon.tech (PostgreSQL en la nube)


## Configuración
Crea el archivo `src/main/resources/application.yml` con tus credenciales:

```yaml
spring:
  application:
    name: ms-reportes
  datasource:
    url: jdbc:postgresql:///ms-reportes?sslmode=require&channelBinding=require
    username: 
    password: 
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


## Ejecutar pruebas unitarias
```bash
./mvnw test
```


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

### Buscar por ID
GET /api/reportes/{id}

### Actualizar estado
PUT /api/reportes/{id}/estado
Content-Type: application/json
{
"estado": "EN_ATENCION"
}

### Eliminar reporte
DELETE /api/reportes/{id}


## Migraciones de BD (Flyway)
| Versión | Archivo | Descripción |
|---------|---------|-------------|
| V1 | V1__crear_tabla_reporte.sql | Crea tabla reporte con campos id, titulo, descripcion, latitud, longitud, tipo, estado, email_usuario, fecha_creacion |


## Estrategia de Branching (Git Flow)

- `main` → código estable y probado
- `qa` → ambiente de validación previa a producción  
- `develop` → integración de features
- `feature/*` → desarrollo de funcionalidades


## Estructura del proyecto
src/main/java/cl/municipalidad/msreport/
├── controller/
│   └── ReportController.java
├── service/
│   └── ReportService.java
├── repository/
│   └── ReportRepository.java
├── model/
│   └── Report.java
├── dto/
│   └── ReportDTO.java
├── factory/
│   ├── ReportFactory.java
│   └── ReportType.java
└── MsReportApplication.java


## Pruebas unitarias
Las pruebas están organizadas por capa y cubren los casos principales del sistema:

| Clase de prueba | Qué prueba |
|----------------|------------|
| `ReportServiceTest` | Crear reporte, listar activos, buscar por ID, actualizar estado, eliminar |
| `ReportControllerTest` | Endpoints REST con respuestas HTTP correctas |
| `ReportFactoryTest` | Creación correcta de cada tipo de reporte (INCENDIO, HUMO, SOSPECHOSO) |

**Herramientas usadas:**
- **JUnit 5** → framework de pruebas
- **Mockito** → simula dependencias (Repository, Factory) sin tocar la BD real

**Ejemplo de prueba:**
```java
@Test
void crear_reporteIncendio_tieneEstadoActivo() {
    Report reporte = reportFactory.crear(
        "Incendio", "Humo visible", -33.4489, -70.6693, "INCENDIO", "user@test.com"
    );
    assertEquals("ACTIVO", reporte.getEstado());
    assertTrue(reporte.getDescripcion().contains("[PRIORIDAD ALTA]"));
}
```