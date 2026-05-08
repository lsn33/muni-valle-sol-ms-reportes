package cl.municipalidad.ms_reportes.reporte.factory;

import cl.municipalidad.ms_reportes.reporte.Reporte;
import org.springframework.stereotype.Component;

@Component
public class ReporteFactory {

    public Reporte crear(String titulo, String descripcion, 
                         Double latitud, Double longitud,
                         String tipo, String emailUsuario) {
        
        Reporte reporte = new Reporte();
        reporte.setTitulo(titulo);
        reporte.setDescripcion(descripcion);
        reporte.setLatitud(latitud);
        reporte.setLongitud(longitud);
        reporte.setEmailUsuario(emailUsuario);
        reporte.setTipo(tipo.toUpperCase());

        switch (TipoReporte.valueOf(tipo.toUpperCase())) {
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