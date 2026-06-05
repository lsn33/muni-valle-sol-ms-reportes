package cl.municipalidad.msreport.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.municipalidad.msreport.dto.CreateReportRequest;
import cl.municipalidad.msreport.dto.ReportDTO;
import cl.municipalidad.msreport.dto.UpdateEstadoRequest;
import cl.municipalidad.msreport.service.ReportService;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de reportes de emergencia.
 *
 * <p>Expone los endpoints de creación, consulta, actualización y eliminación
 * de reportes. Delega toda la lógica de negocio al {@link ReportService}
 * y usa {@code @Valid} para validar los DTOs de entrada antes de procesarlos.</p>
 *
 * <p>Todos los endpoints son públicos, ya que la autenticación se valida
 * en el BFF antes de llegar a este microservicio.</p>
 *
 * <p><b>Base URL:</b> {@code /api/reportes}</p>
 *
 * @author Municipalidad Valle del Sol
 * @version 1.0
 * @see ReportService
 */
@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reporteService;

    /**
     * Crea un nuevo reporte de emergencia.
     *
     * <p><b>POST</b> {@code /api/reportes}</p>
     *
     * <p>Ejemplo de cuerpo de solicitud:
     * <pre>{@code
     * {
     *   "titulo": "Incendio en Av. Principal",
     *   "descripcion": "Humo visible desde el parque",
     *   "latitud": -33.45,
     *   "longitud": -70.65,
     *   "tipo": "INCENDIO",
     *   "emailUsuario": "vecino@municipalidad.cl"
     * }
     * }</pre></p>
     *
     * @param request DTO validado con los datos del nuevo reporte.
     * @return {@link ReportDTO} con el reporte creado y HTTP 201.
     *         HTTP 400 si algún campo no pasa la validación.
     */
    @PostMapping
    public ResponseEntity<ReportDTO> crear(@Valid @RequestBody CreateReportRequest request) {
        ReportDTO dto = reporteService.crear(
            request.titulo(),
            request.descripcion(),
            request.latitud(),
            request.longitud(),
            request.tipo(),
            request.emailUsuario()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    /**
     * Retorna todos los reportes registrados en el sistema.
     *
     * <p><b>GET</b> {@code /api/reportes}</p>
     *
     * @return HTTP 200 con lista de {@link ReportDTO}. Lista vacía si no hay reportes.
     */
    @GetMapping
    public ResponseEntity<List<ReportDTO>> listarTodos() {
        return ResponseEntity.ok(reporteService.listarTodos());
    }

    /**
     * Retorna solo los reportes con estado {@code ACTIVO}.
     * Usado por el frontend para mostrar marcadores en el mapa Leaflet.
     *
     * <p><b>GET</b> {@code /api/reportes/activos}</p>
     *
     * @return HTTP 200 con lista de {@link ReportDTO} activos.
     */
    @GetMapping("/activos")
    public ResponseEntity<List<ReportDTO>> listarActivos() {
        return ResponseEntity.ok(reporteService.listarActivos());
    }

    /**
     * Busca un reporte específico por su identificador.
     *
     * <p><b>GET</b> {@code /api/reportes/{id}}</p>
     *
     * @param id Identificador del reporte (path variable).
     * @return HTTP 200 con {@link ReportDTO} si existe.
     *         HTTP 409 si no se encuentra el reporte.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReportDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.buscarPorId(id));
    }

    /**
     * Actualiza el estado de un reporte existente.
     *
     * <p><b>PUT</b> {@code /api/reportes/{id}/estado}</p>
     *
     * <p>Ejemplo de cuerpo de solicitud:
     * <pre>{@code
     * {
     *   "estado": "CERRADO"
     * }
     * }</pre></p>
     *
     * @param id      Identificador del reporte a actualizar (path variable).
     * @param request DTO validado con el nuevo estado.
     * @return HTTP 200 con {@link ReportDTO} actualizado.
     *         HTTP 400 si el estado no es válido.
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<ReportDTO> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEstadoRequest request) {
        return ResponseEntity.ok(reporteService.actualizarEstado(id, request.estado()));
    }

    /**
     * Actualiza el título de un reporte existente.
     *
     * <p><b>PUT</b> {@code /api/reportes/{id}}</p>
     *
     * @param id   Identificador del reporte a actualizar (path variable).
     * @param body Mapa con el campo {@code titulo}.
     * @return HTTP 200 con {@link ReportDTO} actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReportDTO> actualizarTitulo(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(reporteService.actualizarTitulo(id, body.get("titulo")));
    }

    /**
     * Elimina un reporte por su identificador.
     *
     * <p><b>DELETE</b> {@code /api/reportes/{id}}</p>
     *
     * @param id Identificador del reporte a eliminar (path variable).
     * @return HTTP 204 sin contenido si se eliminó correctamente.
     *         HTTP 409 si no se encuentra el reporte.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        reporteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}