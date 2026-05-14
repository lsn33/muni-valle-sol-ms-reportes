package cl.municipalidad.msreport.factory;

import org.springframework.stereotype.Component;

import cl.municipalidad.msreport.model.Report;

@Component
public class ReportFactory {

    public Report crear(String titulo, String descripcion, 
                         Double latitud, Double longitud,
                         String tipo, String emailUsuario) {
        
        Report reporte = new Report();
        reporte.setTitulo(titulo);
        reporte.setDescripcion(descripcion);
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