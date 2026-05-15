package cl.municipalidad.msreport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import cl.municipalidad.msreport.dto.ReportDTO;
import cl.municipalidad.msreport.factory.ReportFactory;
import cl.municipalidad.msreport.model.Report;
import cl.municipalidad.msreport.repository.ReportRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reporteRepository;
    private final ReportFactory reporteFactory;

    public ReportDTO crear(String titulo, String descripcion,
                            Double latitud, Double longitud,
                            String tipo, String emailUsuario) {

        Report reporte = reporteFactory.crear(titulo, descripcion,
                                               latitud, longitud,
                                               tipo, emailUsuario);
        Report guardado = reporteRepository.save(reporte);
        return toDTO(guardado);
    }

    public List<ReportDTO> listarActivos() {
        return reporteRepository.findByEstado("ACTIVO")
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<ReportDTO> listarTodos() {
        return reporteRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public ReportDTO buscarPorId(Long id) {
        return reporteRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado con id: " + id));
    }

    public ReportDTO actualizarEstado(Long id, String nuevoEstado) {
        Report reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado con id: " + id));
        reporte.setEstado(nuevoEstado);
        return toDTO(reporteRepository.save(reporte));
    }

    public ReportDTO actualizarTitulo(Long id, String nuevoTitulo) {
        Report reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado con id: " + id));
        reporte.setTitulo(nuevoTitulo);
        return toDTO(reporteRepository.save(reporte));
    }

    public void eliminar(Long id) {
        if (!reporteRepository.existsById(id)) {
            throw new RuntimeException("Reporte no encontrado con id: " + id);
        }
        reporteRepository.deleteById(id);
    }

    private ReportDTO toDTO(Report reporte) {
        return new ReportDTO(
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