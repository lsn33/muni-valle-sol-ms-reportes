package cl.municipalidad.msreport.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO de entrada para actualizar el estado de un reporte existente.
 *
 * <p>Implementa el patrón <b>Record</b> de Java para inmutabilidad.
 * Separa la responsabilidad del {@link CreateReportRequest}, siguiendo
 * el principio de responsabilidad única: cada DTO representa
 * una operación específica.</p>
 *
 * <p><b>Estados válidos:</b>
 * <ul>
 *   <li>{@code ACTIVO} — reporte activo y visible en el mapa</li>
 *   <li>{@code EN_REVISION} — siendo evaluado por un funcionario</li>
 *   <li>{@code PENDIENTE} — pendiente de confirmación</li>
 *   <li>{@code CERRADO} — reporte resuelto y cerrado</li>
 * </ul></p>
 *
 * @param estado Nuevo estado del reporte. Obligatorio, debe ser un valor válido.
 *
 * @author Municipalidad Valle del Sol
 * @version 1.0
 */
public record UpdateEstadoRequest(

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(
        regexp = "ACTIVO|EN_REVISION|PENDIENTE|CERRADO",
        message = "El estado debe ser ACTIVO, EN_REVISION, PENDIENTE o CERRADO"
    )
    String estado

) {}