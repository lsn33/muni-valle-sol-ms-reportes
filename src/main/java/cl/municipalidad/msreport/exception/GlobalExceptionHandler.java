package cl.municipalidad.msreport.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manejador global de excepciones para el microservicio de reportes.
 *
 * <p>Intercepta las excepciones lanzadas en cualquier controlador y las
 * transforma en respuestas HTTP estructuradas y legibles, evitando que
 * Spring devuelva stack traces o mensajes genéricos al cliente.</p>
 *
 * <p><b>Excepciones manejadas:</b>
 * <ul>
 *   <li>{@link MethodArgumentNotValidException} → HTTP 400 (errores de validación @Valid)</li>
 *   <li>{@link IllegalArgumentException} → HTTP 400 (tipo de reporte inválido del factory)</li>
 *   <li>{@link RuntimeException} → HTTP 409 (reporte no encontrado u otras reglas de negocio)</li>
 *   <li>{@link Exception} → HTTP 500 (errores inesperados)</li>
 * </ul></p>
 *
 * @author Municipalidad Valle del Sol
 * @version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja errores de validación de Bean Validation ({@code @Valid}).
     *
     * <p>Ejemplo de respuesta:
     * <pre>{@code
     * {
     *   "error": "El título es obligatorio, El tipo debe ser INCENDIO, HUMO o SOSPECHOSO",
     *   "timestamp": "2025-06-01T12:00:00"
     * }
     * }</pre></p>
     *
     * @param ex Excepción lanzada por Spring cuando falla la validación.
     * @return HTTP 400 con los mensajes de error de cada campo inválido.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildError(mensaje));
    }

    /**
     * Maneja tipos de reporte inválidos lanzados por el {@link cl.municipalidad.msreport.factory.ReportFactory}.
     *
     * <p>Se activa cuando se envía un tipo que no existe en el enum
     * {@link cl.municipalidad.msreport.factory.ReportType}.</p>
     *
     * @param ex Excepción lanzada por el factory al hacer {@code ReportType.valueOf()}.
     * @return HTTP 400 con mensaje descriptivo del error.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildError("Tipo de reporte inválido. Use: INCENDIO, HUMO o SOSPECHOSO"));
    }

    /**
     * Maneja excepciones de reglas de negocio (reporte no encontrado, etc.).
     *
     * @param ex Excepción lanzada por el servicio.
     * @return HTTP 409 Conflict con el mensaje descriptivo del error.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(buildError(ex.getMessage()));
    }

    /**
     * Maneja cualquier excepción no contemplada por los handlers anteriores.
     *
     * @param ex Excepción genérica no manejada.
     * @return HTTP 500 con mensaje genérico.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError("Error interno del servidor. Intente más tarde."));
    }

    /**
     * Construye la estructura estándar de respuesta de error.
     *
     * @param mensaje Descripción del error ocurrido.
     * @return Mapa con los campos {@code error} y {@code timestamp}.
     */
    private Map<String, Object> buildError(String mensaje) {
        return Map.of(
                "error", mensaje,
                "timestamp", LocalDateTime.now().toString()
        );
    }
}