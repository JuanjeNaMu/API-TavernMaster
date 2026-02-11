package dam.tavernmaster.controller;

import dam.tavernmaster.entity.Campana;
import dam.tavernmaster.service.CampanaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/campanas")
public class CampanaController {

    @Autowired
    private CampanaService campanaService;

    // === GET: Todas ===
    @GetMapping
    public List<Campana> obtenerTodas() {
        return campanaService.getAllCampanas();
    }

    // === GET: Por ID (CORREGIDO: idCam) ===
    @GetMapping("/{idCam}")
    public ResponseEntity<Campana> obtenerPorId(@PathVariable Integer idCam) {
        return campanaService.getCampanaById(idCam)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === GET: Con personajes (CORREGIDO: idCam) ===
    @GetMapping("/{idCam}/with-personajes")
    public ResponseEntity<Campana> obtenerConPersonajes(@PathVariable Integer idCam) {
        return campanaService.getCampanaWithPersonajes(idCam)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === GET: Buscar por título ===
    @GetMapping("/buscar/titulo")
    public List<Campana> buscarPorTitulo(@RequestParam String titulo) {
        return campanaService.getCampanasByTitulo(titulo);
    }

    // === GET: Buscar por master ===
    @GetMapping("/buscar/master")
    public List<Campana> buscarPorMaster(@RequestParam String master) {
        return campanaService.getCampanasByMaster(master);
    }

    // === GET: Por fecha (próxima sesión) ===
    @GetMapping("/proxima-sesion/after")
    public List<Campana> obtenerProximaSesionAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return campanaService.getCampanasByProximaSesionAfter(fecha);
    }

    @GetMapping("/proxima-sesion/between")
    public List<Campana> obtenerProximaSesionBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return campanaService.getCampanasByProximaSesionBetween(start, end);
    }

    @GetMapping("/proxima-sesion/null")
    public List<Campana> obtenerSinProximaSesion() {
        return campanaService.getCampanasSinProximaSesion();
    }

    @GetMapping("/proxima-sesion/not-null")
    public List<Campana> obtenerConProximaSesion() {
        return campanaService.getCampanasConProximaSesion();
    }

    // === GET: Por encuentros ===
    @GetMapping("/activas")
    public List<Campana> obtenerActivas() {
        return campanaService.getCampanasActivas();
    }

    @GetMapping("/finalizadas")
    public List<Campana> obtenerFinalizadas() {
        return campanaService.getCampanasFinalizadas();
    }

    @GetMapping("/encuentros/{valor}")
    public List<Campana> obtenerPorEncuentros(@PathVariable Integer valor) {
        return campanaService.getCampanasByEncuentros(valor);
    }

    // === POST: Crear ===
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Campana crear(@RequestBody Campana campana) {
        // Asegurar que el ID es nulo para que la BD lo genere
        campana.setIdCam(null);
        return campanaService.saveCampana(campana);
    }

    // === PUT: Actualizar (CORREGIDO: idCam) ===
    @PutMapping("/{idCam}")
    public ResponseEntity<Campana> actualizar(@PathVariable Integer idCam, @RequestBody Campana campana) {
        return campanaService.updateCampana(idCam, campana)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === PATCH: Finalizar (CORREGIDO: idCam) ===
    @PatchMapping("/{idCam}/finalizar")
    public ResponseEntity<Campana> finalizar(@PathVariable Integer idCam) {
        return campanaService.finalizarCampana(idCam)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === DELETE: Eliminar (CORREGIDO: idCam) ===
    @DeleteMapping("/{idCam}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idCam) {
        if (campanaService.getCampanaById(idCam).isPresent()) {
            campanaService.deleteCampana(idCam);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // === GET: Estadísticas ===
    @GetMapping("/estadisticas/master")
    public List<Object[]> obtenerEstadisticasPorMaster() {
        return campanaService.getCampanasCountByMaster();
    }
}