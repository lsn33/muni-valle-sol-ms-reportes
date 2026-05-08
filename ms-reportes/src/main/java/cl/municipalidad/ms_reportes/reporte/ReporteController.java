package cl.municipalidad.ms_reportes.reporte;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @PostMapping
    public ResponseEntity<ReporteDTO> crear(@RequestBody Map<String, Object> body) {
        ReporteDTO dto = reporteService.crear(
            (String) body.get("titulo"),
            (String) body.get("descripcion"),
            Double.valueOf(body.get("latitud").toString()),
            Double.valueOf(body.get("longitud").toString()),
            (String) body.get("tipo"),
            (String) body.get("emailUsuario")
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping
    public ResponseEntity<List<ReporteDTO>> listarTodos() {
        return ResponseEntity.ok(reporteService.listarTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ReporteDTO>> listarActivos() {
        return ResponseEntity.ok(reporteService.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReporteDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.buscarPorId(id));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<ReporteDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(reporteService.actualizarEstado(id, body.get("estado")));
    }
}