package cl.municipalidad.msreport.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.municipalidad.msreport.dto.ReportDTO;
import cl.municipalidad.msreport.service.ReportService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reporteService;

    @PostMapping
    public ResponseEntity<ReportDTO> crear(@RequestBody Map<String, Object> body) {
        ReportDTO dto = reporteService.crear(
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
    public ResponseEntity<List<ReportDTO>> listarTodos() {
        return ResponseEntity.ok(reporteService.listarTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ReportDTO>> listarActivos() {
        return ResponseEntity.ok(reporteService.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.buscarPorId(id));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<ReportDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(reporteService.actualizarEstado(id, body.get("estado")));
    }
}