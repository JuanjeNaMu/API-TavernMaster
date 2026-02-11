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

    // Este es el controlador REST de Campañas
    // Aquí llegan las peticiones HTTP y se las paso al Service

    // === GET: Todas ===
    @GetMapping
    public List<Campana> obtenerTodas() {
        // GET a /api/campanas
        // Devuelve todas las campañas
        return campanaService.getAllCampanas();
    }

    // === GET: Por ID (CORREGIDO: idCam) ===
    @GetMapping("/{idCam}")
    public ResponseEntity<Campana> obtenerPorId(@PathVariable Integer idCam) {
        // GET a /api/campanas/5
        // Devuelve la campaña con ID = 5, o 404 si no existe
        return campanaService.getCampanaById(idCam)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === GET: Con personajes (CORREGIDO: idCam) ===
    @GetMapping("/{idCam}/with-personajes")
    public ResponseEntity<Campana> obtenerConPersonajes(@PathVariable Integer idCam) {
        // GET a /api/campanas/5/with-personajes
        // Devuelve la campaña con sus personajes cargados
        return campanaService.getCampanaWithPersonajes(idCam)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === GET: Buscar por título ===
    @GetMapping("/buscar/titulo")
    public List<Campana> buscarPorTitulo(@RequestParam String titulo) {
        // GET a /api/campanas/buscar/titulo?titulo=LaMansión
        // Busca campañas que contengan ese título
        return campanaService.getCampanasByTitulo(titulo);
    }

    // === GET: Buscar por master ===
    @GetMapping("/buscar/master")
    public List<Campana> buscarPorMaster(@RequestParam String master) {
        // GET a /api/campanas/buscar/master?master=Carlos
        // Busca campañas dirigidas por ese master
        return campanaService.getCampanasByMaster(master);
    }

    // === GET: Por fecha (próxima sesión) ===
    @GetMapping("/proxima-sesion/after")
    public List<Campana> obtenerProximaSesionAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        // GET a /api/campanas/proxima-sesion/after?fecha=2025-03-15
        // Busca campañas con próxima sesión después de esa fecha
        return campanaService.getCampanasByProximaSesionAfter(fecha);
    }

    @GetMapping("/proxima-sesion/between")
    public List<Campana> obtenerProximaSesionBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        // GET a /api/campanas/proxima-sesion/between?start=2025-03-01&end=2025-03-31
        // Busca campañas con próxima sesión entre esas fechas
        return campanaService.getCampanasByProximaSesionBetween(start, end);
    }

    @GetMapping("/proxima-sesion/null")
    public List<Campana> obtenerSinProximaSesion() {
        // GET a /api/campanas/proxima-sesion/null
        // Campañas que no tienen próxima sesión programada
        return campanaService.getCampanasSinProximaSesion();
    }

    @GetMapping("/proxima-sesion/not-null")
    public List<Campana> obtenerConProximaSesion() {
        // GET a /api/campanas/proxima-sesion/not-null
        // Campañas que SÍ tienen próxima sesión programada
        return campanaService.getCampanasConProximaSesion();
    }

    // === GET: Por encuentros ===

    @GetMapping("/encuentros/{valor}")
    public List<Campana> obtenerPorEncuentros(@PathVariable Integer valor) {
        // GET a /api/campanas/encuentros/10
        // Busca campañas con exactamente 10 encuentros
        return campanaService.getCampanasByEncuentros(valor);
    }

    // === POST: Crear ===
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Campana crear(@RequestBody Campana campana) {
        // POST a /api/campanas
        // Body: JSON con los datos de la campaña
        // El ID se pone a null para que la BD lo genere automático
        campana.setIdCam(null);
        return campanaService.saveCampana(campana);
    }

    // === PUT: Actualizar (CORREGIDO: idCam) ===
    @PutMapping("/{idCam}")
    public ResponseEntity<Campana> actualizar(@PathVariable Integer idCam, @RequestBody Campana campana) {
        // PUT a /api/campanas/5
        // Body: JSON con los datos actualizados
        // Actualiza la campaña con ID = 5
        return campanaService.updateCampana(idCam, campana)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === PATCH: Finalizar (CORREGIDO: idCam) ===
    @PatchMapping("/{idCam}/finalizar")
    public ResponseEntity<Campana> finalizar(@PathVariable Integer idCam) {
        // PATCH a /api/campanas/5/finalizar
        // Marca la campaña como finalizada
        return campanaService.finalizarCampana(idCam)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === DELETE: Eliminar (CORREGIDO: idCam) ===
    @DeleteMapping("/{idCam}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idCam) {
        // DELETE a /api/campanas/5
        // Elimina la campaña si existe
        if (campanaService.getCampanaById(idCam).isPresent()) {
            campanaService.deleteCampana(idCam);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // === GET: Estadísticas ===
    @GetMapping("/estadisticas/master")
    public List<Object[]> obtenerEstadisticasPorMaster() {
        // GET a /api/campanas/estadisticas/master
        // Devuelve cuántas campañas ha hecho cada master
        return campanaService.getCampanasCountByMaster();
    }
}