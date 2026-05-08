package cl.municipalidad.ms_reportes.reporte;

import cl.municipalidad.ms_reportes.reporte.factory.ReporteFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final ReporteRepository reporteRepository;
    private final ReporteFactory reporteFactory;

    public ReporteDTO crear(String titulo, String descripcion,
                            Double latitud, Double longitud,
                            String tipo, String emailUsuario) {

        Reporte reporte = reporteFactory.crear(titulo, descripcion, 
                                               latitud, longitud, 
                                               tipo, emailUsuario);
        Reporte guardado = reporteRepository.save(reporte);
        return toDTO(guardado);
    }

    public List<ReporteDTO> listarActivos() {
        return reporteRepository.findByEstado("ACTIVO")
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<ReporteDTO> listarTodos() {
        return reporteRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public ReporteDTO buscarPorId(Long id) {
        return reporteRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado con id: " + id));
    }

    public ReporteDTO actualizarEstado(Long id, String nuevoEstado) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado con id: " + id));
        reporte.setEstado(nuevoEstado);
        return toDTO(reporteRepository.save(reporte));
    }

    private ReporteDTO toDTO(Reporte reporte) {
        return new ReporteDTO(
                reporte.getId(),
                reporte.getTitulo(),
                reporte.getDescripcion(),
                reporte.getLatitud(),
                reporte.getLongitud(),
                reporte.getTipo(),
                reporte.getEstado(),
                reporte.getEmailUsuario(),
                reporte.getFechaCreacion()
        );
    }
}