package cl.municipalidad.msreport.dto;

import java.time.LocalDateTime;

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