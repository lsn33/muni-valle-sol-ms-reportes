package cl.municipalidad.msreport.dto;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) de salida que representa un reporte de emergencia.
 *
 * <p>Implementa el patrón <b>Record</b> de Java, garantizando inmutabilidad
 * y eliminando código repetitivo. Se usa para transferir datos desde el
 * servicio hacia el controlador y finalmente al cliente (BFF/Frontend),
 * sin exponer la entidad {@link cl.municipalidad.msreport.model.Report}
 * directamente.</p>
 *
 * <p><b>Patrón aplicado:</b> Data Transfer Object (DTO) con Java Record.</p>
 *
 * @param id            Identificador único del reporte.
 * @param titulo        Título descriptivo del reporte.
 * @param descripcion   Descripción completa con etiqueta de prioridad agregada por el factory.
 * @param latitud       Coordenada de latitud de la ubicación del reporte.
 * @param longitud      Coordenada de longitud de la ubicación del reporte.
 * @param tipo          Tipo de reporte: INCENDIO, HUMO o SOSPECHOSO.
 * @param estado        Estado actual: ACTIVO, EN_REVISION, PENDIENTE o CERRADO.
 * @param emailUsuario  Correo del usuario que creó el reporte.
 * @param fechaCreacion Fecha y hora de creación del reporte.
 *
 * @author Municipalidad Valle del Sol
 * @version 1.0
 */
public record ReportDTO(
    Long id,
    String titulo,
    String descripcion,
    Double latitud,
    Double longitud,
    String tipo,
    String estado,
    String emailUsuario,
    LocalDateTime fechaCreacion
) {}