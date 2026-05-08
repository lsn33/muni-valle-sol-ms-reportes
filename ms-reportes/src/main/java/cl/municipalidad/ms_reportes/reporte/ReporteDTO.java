package cl.municipalidad.ms_reportes.reporte;

import java.time.LocalDateTime;

public record ReporteDTO(
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