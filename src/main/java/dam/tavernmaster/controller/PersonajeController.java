package dam.tavernmaster.controller;

import dam.tavernmaster.entity.Personaje;
import dam.tavernmaster.service.PersonajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personajes")
public class PersonajeController {

    @Autowired
    private PersonajeService personajeService;

    // === GET: Todos ===
    @GetMapping
    public List<Personaje> obtenerTodos() {
        return personajeService.getAllPersonajes();
    }

    // === GET: Por ID (CORREGIDO: idPer) ===
    @GetMapping("/{idPer}")
    public ResponseEntity<Personaje> obtenerPorId(@PathVariable Integer idPer) {
        return personajeService.getPersonajeById(idPer)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === GET: Por jugador padre ===
    @GetMapping("/buscar/jugador-padre")
    public List<Personaje> obtenerPorJugadorPadre(@RequestParam String jugadorPadre) {
        return personajeService.getPersonajesByJugadorPadre(jugadorPadre);
    }

    // === GET: Por campaña (CORREGIDO: idCam) ===
    @GetMapping("/campana/{idCam}")
    public List<Personaje> obtenerPorCampanaId(@PathVariable Integer idCam) {
        return personajeService.getPersonajesByCampanaId(idCam);
    }

    // === GET: Por nivel mínimo ===
    @GetMapping("/nivel/minimo/{nivel}")
    public List<Personaje> obtenerPorNivelMinimo(@PathVariable Integer nivel) {
        return personajeService.getPersonajesByNivelMinimo(nivel);
    }

    // === POST: Crear ===
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Personaje crear(@RequestBody Personaje personaje) {
        // Asegurar que el ID es nulo para que la BD lo genere
        personaje.setIdPer(null);
        return personajeService.savePersonaje(personaje);
    }

    // === PUT: Actualizar (CORREGIDO: idPer) ===
    @PutMapping("/{idPer}")
    public ResponseEntity<Personaje> actualizar(@PathVariable Integer idPer, @RequestBody Personaje personaje) {
        return personajeService.updatePersonaje(idPer, personaje)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === DELETE: Eliminar (CORREGIDO: idPer) ===
    @DeleteMapping("/{idPer}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idPer) {
        if (personajeService.getPersonajeById(idPer).isPresent()) {
            personajeService.deletePersonaje(idPer);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // === PATCH: Asignar campaña (CORREGIDO: idPer, idCam) ===
    @PatchMapping("/{idPer}/asignar-campana/{idCam}")
    public ResponseEntity<Personaje> asignarCampana(@PathVariable Integer idPer, @PathVariable Integer idCam) {
        return personajeService.asignarCampana(idPer, idCam)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}