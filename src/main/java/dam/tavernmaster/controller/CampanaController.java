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

    // === GET: Por participante ===
    @GetMapping("/participante/{nombreJug}")
    public List<Campana> obtenerPorParticipante(@PathVariable String nombreJug) {
        return campanaService.getCampanasByParticipante(nombreJug);
    }
}