package cl.municipalidad.msreport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import cl.municipalidad.msreport.dto.ReportDTO;
import cl.municipalidad.msreport.factory.ReportFactory;
import cl.municipalidad.msreport.model.Report;
import cl.municipalidad.msreport.repository.ReportRepository;

import java.util.List;

/**
 * Servicio de lógica de negocio para la gestión de reportes de emergencia.
 *
 * <p>Actúa como capa intermedia entre el controlador
 * ({@link cl.municipalidad.msreport.controller.ReportController}) y el repositorio
 * ({@link ReportRepository}), delegando la creación de entidades al
 * {@link ReportFactory} y la conversión a DTO al método privado {@code toDTO()}.</p>
 *
 * <p>Utiliza {@code @RequiredArgsConstructor} de Lombok para inyección de
 * dependencias por constructor, siguiendo las buenas prácticas de Spring.</p>
 *
 * @author Municipalidad Valle del Sol
 * @version 1.0
 * @see ReportFactory
 * @see ReportRepository
 * @see ReportDTO
 */
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reporteRepository;
    private final ReportFactory reporteFactory;

    /**
     * Crea un nuevo reporte de emergencia.
     *
     * <p>Delega la construcción de la entidad al {@link ReportFactory},
     * que asigna el estado inicial y la prioridad según el tipo de reporte.
     * Luego persiste el reporte y retorna su representación como DTO.</p>
     *
     * @param titulo       Título del reporte.
     * @param descripcion  Descripción base (el factory agrega la etiqueta de prioridad).
     * @param latitud      Coordenada de latitud.
     * @param longitud     Coordenada de longitud.
     * @param tipo         Tipo de reporte: INCENDIO, HUMO o SOSPECHOSO.
     * @param emailUsuario Correo del usuario que crea el reporte.
     * @return {@link ReportDTO} con los datos del reporte creado.
     */
    public ReportDTO crear(String titulo, String descripcion,
                           Double latitud, Double longitud,
                           String tipo, String emailUsuario) {
        Report reporte = reporteFactory.crear(titulo, descripcion, latitud, longitud, tipo, emailUsuario);
        return toDTO(reporteRepository.save(reporte));
    }

    /**
     * Retorna todos los reportes con estado {@code ACTIVO}.
     * Usado por el frontend para mostrar marcadores en el mapa Leaflet.
     *
     * @return Lista de {@link ReportDTO} con reportes activos. Vacía si no hay ninguno.
     */
    public List<ReportDTO> listarActivos() {
        return reporteRepository.findByEstado("ACTIVO")
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /**
     * Retorna todos los reportes sin filtro de estado.
     *
     * @return Lista completa de {@link ReportDTO}.
     */
    public List<ReportDTO> listarTodos() {
        return reporteRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /**
     * Busca un reporte por su identificador único.
     *
     * @param id Identificador del reporte.
     * @return {@link ReportDTO} con los datos del reporte encontrado.
     * @throws RuntimeException si no existe un reporte con el id dado.
     */
    public ReportDTO buscarPorId(Long id) {
        return reporteRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado con id: " + id));
    }

    /**
     * Actualiza el estado de un reporte existente.
     *
     * @param id          Identificador del reporte a actualizar.
     * @param nuevoEstado Nuevo estado a asignar (ACTIVO, EN_REVISION, PENDIENTE, CERRADO).
     * @return {@link ReportDTO} con el estado actualizado.
     * @throws RuntimeException si no existe un reporte con el id dado.
     */
    public ReportDTO actualizarEstado(Long id, String nuevoEstado) {
        Report reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado con id: " + id));
        reporte.setEstado(nuevoEstado);
        return toDTO(reporteRepository.save(reporte));
    }

    /**
     * Actualiza el título de un reporte existente.
     *
     * @param id          Identificador del reporte a actualizar.
     * @param nuevoTitulo Nuevo título a asignar.
     * @return {@link ReportDTO} con el título actualizado.
     * @throws RuntimeException si no existe un reporte con el id dado.
     */
    public ReportDTO actualizarTitulo(Long id, String nuevoTitulo) {
        Report reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado con id: " + id));
        reporte.setTitulo(nuevoTitulo);
        return toDTO(reporteRepository.save(reporte));
    }

    /**
     * Elimina un reporte por su identificador único.
     *
     * @param id Identificador del reporte a eliminar.
     * @throws RuntimeException si no existe un reporte con el id dado.
     */
    public void eliminar(Long id) {
        if (!reporteRepository.existsById(id)) {
            throw new RuntimeException("Reporte no encontrado con id: " + id);
        }
        reporteRepository.deleteById(id);
    }

    /**
     * Convierte una entidad {@link Report} a su representación pública {@link ReportDTO}.
     *
     * <p>Método privado auxiliar que centraliza la conversión, evitando
     * duplicación de código en los distintos métodos del servicio.</p>
     *
     * @param reporte Entidad a convertir.
     * @return {@link ReportDTO} con los datos del reporte.
     */
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