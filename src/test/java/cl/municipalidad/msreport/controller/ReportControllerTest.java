package cl.municipalidad.msreport.controller;

import cl.municipalidad.msreport.dto.ReportDTO;
import cl.municipalidad.msreport.service.ReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=none",
    "spring.flyway.enabled=false"
})
@DisplayName("ReportController - Pruebas unitarias (capa web)")
class ReportControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private ReportService reporteService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc() {
        return MockMvcBuilders.webAppContextSetup(context).build();
    }

    private ReportDTO reporteDTOMock() {
        return new ReportDTO(1L, "Incendio Av. Principal",
                "Humo visible [PRIORIDAD ALTA]", -33.45, -70.65,
                "INCENDIO", "ACTIVO", "vecino@test.cl", LocalDateTime.now());
    }

    @Test
    @DisplayName("crear: debe retornar 201 al crear un reporte correctamente")
    void crear_exitoso_retorna201() throws Exception {
        when(reporteService.crear(any(), any(), any(), any(), any(), any()))
                .thenReturn(reporteDTOMock());

        Map<String, Object> body = Map.of(
                "titulo", "Incendio Av. Principal",
                "descripcion", "Humo visible",
                "latitud", -33.45,
                "longitud", -70.65,
                "tipo", "INCENDIO",
                "emailUsuario", "vecino@test.cl"
        );

        mockMvc().perform(post("/api/reportes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tipo").value("INCENDIO"))
                .andExpect(jsonPath("$.estado").value("ACTIVO"));
    }

    @Test
    @DisplayName("listarTodos: debe retornar 200 con lista completa")
    void listarTodos_retorna200() throws Exception {
        when(reporteService.listarTodos()).thenReturn(List.of(reporteDTOMock()));

        mockMvc().perform(get("/api/reportes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("listarActivos: debe retornar 200 con solo activos")
    void listarActivos_retorna200() throws Exception {
        when(reporteService.listarActivos()).thenReturn(List.of(reporteDTOMock()));

        mockMvc().perform(get("/api/reportes/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("ACTIVO"));
    }

    @Test
    @DisplayName("buscarPorId: debe retornar 200 cuando el id existe")
    void buscarPorId_retorna200() throws Exception {
        when(reporteService.buscarPorId(1L)).thenReturn(reporteDTOMock());

        mockMvc().perform(get("/api/reportes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("actualizarEstado: debe retornar 200 con estado actualizado")
    void actualizarEstado_retorna200() throws Exception {
        ReportDTO actualizado = new ReportDTO(1L, "T", "D", -33.45, -70.65,
                "INCENDIO", "CERRADO", "v@test.cl", LocalDateTime.now());
        when(reporteService.actualizarEstado(eq(1L), eq("CERRADO"))).thenReturn(actualizado);

        mockMvc().perform(put("/api/reportes/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"estado\":\"CERRADO\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CERRADO"));
    }

    
}