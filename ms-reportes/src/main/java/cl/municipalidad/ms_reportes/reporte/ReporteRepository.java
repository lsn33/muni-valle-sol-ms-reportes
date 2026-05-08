package cl.municipalidad.ms_reportes.reporte;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    
    List<Reporte> findByEstado(String estado);
    
    List<Reporte> findByEmailUsuario(String emailUsuario);
    
    List<Reporte> findByTipo(String tipo);
}