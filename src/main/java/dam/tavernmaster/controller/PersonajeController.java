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

    // Controlador REST de Personajes
    // Aquí llegan las peticiones sobre personajes y las paso al Service

    // === GET: Todos ===
    @GetMapping
    public List<Personaje> obtenerTodos() {
        // GET a /api/personajes
        // Devuelve todos los personajes
        return personajeService.getAllPersonajes();
    }

    // === GET: Por ID (CORREGIDO: idPer) ===
    @GetMapping("/{idPer}")
    public ResponseEntity<Personaje> obtenerPorId(@PathVariable Integer idPer) {
        // GET a /api/personajes/7
        // Devuelve el personaje con ID = 7
        return personajeService.getPersonajeById(idPer)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === GET: Por jugador padre ===
    @GetMapping("/buscar/jugador-padre")
    public List<Personaje> obtenerPorJugadorPadre(@RequestParam String jugadorPadre) {
        // GET a /api/personajes/buscar/jugador-padre?jugadorPadre=Carlos
        // Busca personajes cuyo jugador padre sea "Carlos"
        return personajeService.getPersonajesByJugadorPadre(jugadorPadre);
    }

    // === GET: Por campaña (CORREGIDO: idCam) ===
    @GetMapping("/campana/{idCam}")
    public List<Personaje> obtenerPorCampanaId(@PathVariable Integer idCam) {
        // GET a /api/personajes/campana/5
        // Busca todos los personajes que pertenecen a la campaña con ID = 5
        return personajeService.getPersonajesByCampanaId(idCam);
    }

    // === GET: Por nivel mínimo ===
    @GetMapping("/nivel/minimo/{nivel}")
    public List<Personaje> obtenerPorNivelMinimo(@PathVariable Integer nivel) {
        // GET a /api/personajes/nivel/minimo/10
        // Personajes con nivel mayor o igual a 10
        return personajeService.getPersonajesByNivelMinimo(nivel);
    }

    // === POST: Crear ===
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Personaje crear(@RequestBody Personaje personaje) {
        // POST a /api/personajes
        // Body: JSON con los datos del personaje
        // El ID se pone a null para que la BD lo genere automático
        personaje.setIdPer(null);
        return personajeService.savePersonaje(personaje);
    }

    // === PUT: Actualizar (CORREGIDO: idPer) ===
    @PutMapping("/{idPer}")
    public ResponseEntity<Personaje> actualizar(@PathVariable Integer idPer, @RequestBody Personaje personaje) {
        // PUT a /api/personajes/7
        // Body: JSON con los datos actualizados
        // Actualiza el personaje con ID = 7
        return personajeService.updatePersonaje(idPer, personaje)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === DELETE: Eliminar (CORREGIDO: idPer) ===
    @DeleteMapping("/{idPer}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idPer) {
        // DELETE a /api/personajes/7
        // Elimina el personaje con ID = 7
        if (personajeService.getPersonajeById(idPer).isPresent()) {
            personajeService.deletePersonaje(idPer);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // === PATCH: Asignar campaña (CORREGIDO: idPer, idCam) ===
    @PatchMapping("/{idPer}/asignar-campana/{idCam}")
    public ResponseEntity<Personaje> asignarCampana(@PathVariable Integer idPer, @PathVariable Integer idCam) {
        // PATCH a /api/personajes/7/asignar-campana/5
        // Asigna el personaje 7 a la campaña 5
        return personajeService.asignarCampana(idPer, idCam)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}