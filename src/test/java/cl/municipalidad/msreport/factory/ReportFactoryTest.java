package cl.municipalidad.msreport.factory;

import cl.municipalidad.msreport.model.Report;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ReportFactory - Pruebas unitarias (Factory Method)")
class ReportFactoryTest {

    private ReportFactory reportFactory;

    @BeforeEach
    void setUp() {
        reportFactory = new ReportFactory();
    }

    @Test
    @DisplayName("crear INCENDIO: debe asignar estado ACTIVO y prioridad alta")
    void crear_incendio_estadoActivoYPrioridadAlta() {
        Report reporte = reportFactory.crear(
                "Incendio en plaza", "Humo visible",
                -33.45, -70.65, "INCENDIO", "vecino@test.cl");

        assertThat(reporte.getEstado()).isEqualTo("ACTIVO");
        assertThat(reporte.getDescripcion()).contains("[PRIORIDAD ALTA]");
        assertThat(reporte.getTipo()).isEqualTo("INCENDIO");
    }

    @Test
    @DisplayName("crear INCENDIO: debe normalizar tipo a mayúsculas")
    void crear_incendio_normalizaAMayusculas() {
        Report reporte = reportFactory.crear(
                "Título", "Desc",
                -33.45, -70.65, "incendio", "user@test.cl");

        assertThat(reporte.getTipo()).isEqualTo("INCENDIO");
    }

    @Test
    @DisplayName("crear HUMO: debe asignar estado EN_REVISION y prioridad media")
    void crear_humo_estadoEnRevisionYPrioridadMedia() {
        Report reporte = reportFactory.crear(
                "Humo sector norte", "Se aprecia humo",
                -33.46, -70.66, "HUMO", "usuario@test.cl");

        assertThat(reporte.getEstado()).isEqualTo("EN_REVISION");
        assertThat(reporte.getDescripcion()).contains("[PRIORIDAD MEDIA]");
        assertThat(reporte.getTipo()).isEqualTo("HUMO");
    }

    @Test
    @DisplayName("crear SOSPECHOSO: debe asignar estado PENDIENTE y prioridad baja")
    void crear_sospechoso_estadoPendienteYPrioridadBaja() {
        Report reporte = reportFactory.crear(
                "Persona sospechosa", "Rondando el edificio",
                -33.47, -70.67, "SOSPECHOSO", "user@test.cl");

        assertThat(reporte.getEstado()).isEqualTo("PENDIENTE");
        assertThat(reporte.getDescripcion()).contains("[PRIORIDAD BAJA]");
        assertThat(reporte.getTipo()).isEqualTo("SOSPECHOSO");
    }

    @Test
    @DisplayName("crear: debe asignar correctamente todos los campos comunes")
    void crear_asignaCamposComunes() {
        Report reporte = reportFactory.crear(
                "Título test", "Descripción test",
                -33.45, -70.65, "INCENDIO", "test@municipalidad.cl");

        assertThat(reporte.getTitulo()).isEqualTo("Título test");
        assertThat(reporte.getLatitud()).isEqualTo(-33.45);
        assertThat(reporte.getLongitud()).isEqualTo(-70.65);
        assertThat(reporte.getEmailUsuario()).isEqualTo("test@municipalidad.cl");
    }

    @Test
    @DisplayName("crear: debe lanzar excepción cuando el tipo no existe")
    void crear_tipoInvalido_lanzaExcepcion() {
        assertThatThrownBy(() ->
                reportFactory.crear("T", "D", -33.0, -70.0, "TIPO_INVALIDO", "e@e.cl"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}