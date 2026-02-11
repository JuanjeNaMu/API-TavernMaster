package dam.tavernmaster.controller;

import dam.tavernmaster.entity.Jugador;
import dam.tavernmaster.entity.Sexo;
import dam.tavernmaster.service.JugadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/jugadores")
public class JugadorController {

    @Autowired
    private JugadorService jugadorService;

    // === GET: Todos ===
    @GetMapping
    public List<Jugador> obtenerTodos() {
        return jugadorService.getAllJugadores();
    }

    // === GET: Por ID ===
    @GetMapping("/{id}")
    public ResponseEntity<Jugador> obtenerPorId(@PathVariable Integer id) {
        return jugadorService.getJugadorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === GET: Con personajes ===
    @GetMapping("/{id}/with-personajes")
    public ResponseEntity<Jugador> obtenerConPersonajes(@PathVariable Integer id) {
        return jugadorService.getJugadorWithPersonajes(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === GET: Buscar por nombre de jugador ===
    @GetMapping("/buscar/nombre")
    public ResponseEntity<Jugador> buscarPorNombreJug(@RequestParam String nombreJug) {
        return jugadorService.getJugadorByNombreJug(nombreJug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar/nombre/contiene")
    public List<Jugador> buscarPorNombreJugContiene(@RequestParam String nombreJug) {
        return jugadorService.getJugadoresByNombreJugContaining(nombreJug);
    }

    // === GET: Buscar por email ===
    @GetMapping("/buscar/email")
    public ResponseEntity<Jugador> buscarPorEmail(@RequestParam String email) {
        return jugadorService.getJugadorByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar/email/contiene")
    public List<Jugador> buscarPorEmailContiene(@RequestParam String email) {
        return jugadorService.getJugadoresByEmailContaining(email);
    }

    // === GET: Por tipo de admin ===
    @GetMapping("/admin")
    public List<Jugador> obtenerAdmin() {
        return jugadorService.getJugadoresAdmin();
    }

    @GetMapping("/no-admin")
    public List<Jugador> obtenerNoAdmin() {
        return jugadorService.getJugadoresNoAdmin();
    }

    // === GET: Por fecha de nacimiento ===
    @GetMapping("/fecha-nac/before")
    public List<Jugador> obtenerFechaNacBefore(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return jugadorService.getJugadoresByFechaNacBefore(fecha);
    }

    @GetMapping("/fecha-nac/after")
    public List<Jugador> obtenerFechaNacAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return jugadorService.getJugadoresByFechaNacAfter(fecha);
    }

    @GetMapping("/fecha-nac/between")
    public List<Jugador> obtenerFechaNacBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return jugadorService.getJugadoresByFechaNacBetween(start, end);
    }

    @GetMapping("/fecha-nac/null")
    public List<Jugador> obtenerSinFechaNac() {
        return jugadorService.getJugadoresSinFechaNac();
    }

    @GetMapping("/fecha-nac/not-null")
    public List<Jugador> obtenerConFechaNac() {
        return jugadorService.getJugadoresConFechaNac();
    }

    // === GET: Por sexo ===
    @GetMapping("/sexo/{sexo}")
    public List<Jugador> obtenerPorSexo(@PathVariable Sexo sexo) {
        return jugadorService.getJugadoresBySexo(sexo);
    }

    // === POST: Login ===
    @PostMapping("/login")
    public ResponseEntity<Jugador> login(@RequestParam String nombreJug, @RequestParam String password) {
        return jugadorService.login(nombreJug, password)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    // === POST: Crear ===
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Jugador crear(@RequestBody Jugador jugador) {
        return jugadorService.saveJugador(jugador);
    }

    // === PUT: Actualizar ===
    @PutMapping("/{id}")
    public ResponseEntity<Jugador> actualizar(@PathVariable Integer id, @RequestBody Jugador jugador) {
        return jugadorService.updateJugador(id, jugador)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === DELETE: Eliminar ===
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (jugadorService.getJugadorById(id).isPresent()) {
            jugadorService.deleteJugador(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // === GET: Estadísticas ===
    @GetMapping("/estadisticas/admin")
    public List<Object[]> obtenerEstadisticasPorAdmin() {
        return jugadorService.getCountByEsAdmin();
    }

    @GetMapping("/estadisticas/sexo")
    public List<Object[]> obtenerEstadisticasPorSexo() {
        return jugadorService.getCountBySexo();
    }
}