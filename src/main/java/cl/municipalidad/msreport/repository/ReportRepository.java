package cl.municipalidad.msreport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.municipalidad.msreport.model.Report;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    
    List<Report> findByEstado(String estado);
    
    List<Report> findByEmailUsuario(String emailUsuario);
    
    List<Report> findByTipo(String tipo);
}