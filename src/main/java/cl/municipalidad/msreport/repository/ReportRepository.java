package cl.municipalidad.msreport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.municipalidad.msreport.model.Report;

import java.util.List;

/**
 * Repositorio de acceso a datos para la entidad {@link Report}.
 *
 * <p>Implementa el patrón <b>Repository</b>: define una interfaz de acceso
 * a datos sin exponer detalles de implementación (SQL, JDBC, etc.).
 * Spring Data JPA genera automáticamente la implementación en tiempo de
 * ejecución a partir de los nombres de los métodos.</p>
 *
 * <p>Extiende {@link JpaRepository} para heredar operaciones CRUD básicas:
 * {@code save()}, {@code findById()}, {@code findAll()}, {@code deleteById()},
 * entre otras.</p>
 *
 * <p><b>Patrón aplicado:</b> Repository Pattern (Martin Fowler).</p>
 *
 * @author Municipalidad Valle del Sol
 * @version 1.0
 * @see Report
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    /**
     * Busca todos los reportes con un estado específico.
     * Usado principalmente para listar reportes activos en el mapa.
     *
     * @param estado Estado a filtrar (ej: "ACTIVO", "CERRADO").
     * @return Lista de reportes con el estado dado. Vacía si no hay resultados.
     */
    List<Report> findByEstado(String estado);

    /**
     * Busca todos los reportes creados por un usuario específico.
     *
     * @param emailUsuario Correo del usuario cuyos reportes se quieren obtener.
     * @return Lista de reportes del usuario. Vacía si no tiene reportes.
     */
    List<Report> findByEmailUsuario(String emailUsuario);

    /**
     * Busca todos los reportes de un tipo específico.
     *
     * @param tipo Tipo de reporte a filtrar (ej: "INCENDIO", "HUMO").
     * @return Lista de reportes del tipo dado. Vacía si no hay resultados.
     */
    List<Report> findByTipo(String tipo);
}