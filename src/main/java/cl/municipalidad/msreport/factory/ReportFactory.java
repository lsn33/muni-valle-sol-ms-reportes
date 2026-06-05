package cl.municipalidad.msreport.factory;

import org.springframework.stereotype.Component;
import cl.municipalidad.msreport.model.Report;

/**
 * Factory de creación de reportes de emergencia.
 *
 * <p>Implementa el patrón <b>Factory Method</b>: centraliza la lógica de
 * creación de objetos {@link Report}, asignando automáticamente el estado
 * inicial y la etiqueta de prioridad según el tipo de reporte, sin que
 * el servicio ni el controlador conozcan estas reglas.</p>
 *
 * <p>De esta forma, si se agrega un nuevo tipo de reporte en el futuro,
 * solo se modifica esta clase y el enum {@link ReportType}, sin tocar
 * el resto del sistema.</p>
 *
 * <p><b>Patrón aplicado:</b> Factory Method (GoF).</p>
 *
 * <p><b>Reglas de negocio aplicadas:</b>
 * <ul>
 *   <li>INCENDIO → estado {@code ACTIVO}, prioridad {@code ALTA}</li>
 *   <li>HUMO → estado {@code EN_REVISION}, prioridad {@code MEDIA}</li>
 *   <li>SOSPECHOSO → estado {@code PENDIENTE}, prioridad {@code BAJA}</li>
 * </ul></p>
 *
 * @author Municipalidad Valle del Sol
 * @version 1.0
 * @see ReportType
 * @see Report
 */
@Component
public class ReportFactory {

    /**
     * Crea y configura un nuevo reporte de emergencia según su tipo.
     *
     * <p>Asigna automáticamente el estado inicial y agrega la etiqueta
     * de prioridad a la descripción. El tipo se normaliza a mayúsculas
     * para evitar errores por capitalización inconsistente.</p>
     *
     * @param titulo       Título descriptivo del reporte.
     * @param descripcion  Descripción base de la emergencia. Se le agrega la prioridad.
     * @param latitud      Coordenada de latitud de la ubicación.
     * @param longitud     Coordenada de longitud de la ubicación.
     * @param tipo         Tipo de reporte (INCENDIO, HUMO, SOSPECHOSO). Case-insensitive.
     * @param emailUsuario Correo del usuario que reporta la emergencia.
     * @return {@link Report} configurado con estado y prioridad según el tipo.
     * @throws IllegalArgumentException si el tipo no corresponde a ningún valor de {@link ReportType}.
     */
    public Report crear(String titulo, String descripcion,
                        Double latitud, Double longitud,
                        String tipo, String emailUsuario) {

        Report reporte = new Report();
        reporte.setTitulo(titulo);
        reporte.setLatitud(latitud);
        reporte.setLongitud(longitud);
        reporte.setEmailUsuario(emailUsuario);
        reporte.setTipo(tipo.toUpperCase());

        switch (ReportType.valueOf(tipo.toUpperCase())) {
            case INCENDIO -> {
                reporte.setEstado("ACTIVO");
                reporte.setDescripcion(descripcion + " [PRIORIDAD ALTA]");
            }
            case HUMO -> {
                reporte.setEstado("EN_REVISION");
                reporte.setDescripcion(descripcion + " [PRIORIDAD MEDIA]");
            }
            case SOSPECHOSO -> {
                reporte.setEstado("PENDIENTE");
                reporte.setDescripcion(descripcion + " [PRIORIDAD BAJA]");
            }
        }

        return reporte;
    }
}