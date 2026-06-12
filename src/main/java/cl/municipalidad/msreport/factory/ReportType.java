package cl.municipalidad.msreport.factory;

/**
 * Enumeración de los tipos de reporte de emergencia soportados por el sistema.
 *
 * <p>Usado por {@link ReportFactory} para determinar el estado inicial
 * y la prioridad de cada reporte mediante el patrón Factory Method.</p>
 *
 * <p><b>Prioridades asignadas:</b>
 * <ul>
 *   <li>{@code INCENDIO} → Prioridad ALTA, estado inicial: ACTIVO</li>
 *   <li>{@code HUMO} → Prioridad MEDIA, estado inicial: EN_REVISION</li>
 *   <li>{@code SOSPECHOSO} → Prioridad BAJA, estado inicial: PENDIENTE</li>
 * </ul></p>
 *
 * @author Municipalidad Valle del Sol
 * @version 1.0
 * @see ReportFactory
 */
public enum ReportType {

    /** Incendio confirmado o en curso. Máxima prioridad. */
    INCENDIO,

    /** Presencia de humo sin llamas visibles. Prioridad media. */
    HUMO,

    /** Actividad sospechosa que podría derivar en emergencia. Prioridad baja. */
    SOSPECHOSO
}