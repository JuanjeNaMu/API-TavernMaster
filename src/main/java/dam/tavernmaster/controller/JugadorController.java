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

    // Controlador REST de Jugadores
    // Aquí llegan las peticiones sobre jugadores y las paso al Service

    // === GET: Todos ===
    @GetMapping
    public List<Jugador> obtenerTodos() {
        // GET a /api/jugadores
        // Devuelve todos los jugadores
        return jugadorService.getAllJugadores();
    }

    // === GET: Por ID ===
    @GetMapping("/{id}")
    public ResponseEntity<Jugador> obtenerPorId(@PathVariable Integer id) {
        // GET a /api/jugadores/3
        // Devuelve el jugador con ID = 3
        return jugadorService.getJugadorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === GET: Con personajes ===
    @GetMapping("/{id}/with-personajes")
    public ResponseEntity<Jugador> obtenerConPersonajes(@PathVariable Integer id) {
        // GET a /api/jugadores/3/with-personajes
        // Devuelve el jugador con sus personajes cargados
        return jugadorService.getJugadorWithPersonajes(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === GET: Buscar por nombre de jugador ===
    @GetMapping("/buscar/nombre")
    public ResponseEntity<Jugador> buscarPorNombreJug(@RequestParam String nombreJug) {
        // GET a /api/jugadores/buscar/nombre?nombreJug=Carlos
        // Busca un jugador por su nombre exacto
        return jugadorService.getJugadorByNombreJug(nombreJug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar/nombre/contiene")
    public List<Jugador> buscarPorNombreJugContiene(@RequestParam String nombreJug) {
        // GET a /api/jugadores/buscar/nombre/contiene?nombreJug=Car
        // Busca jugadores que contengan "Car" en el nombre
        return jugadorService.getJugadoresByNombreJugContaining(nombreJug);
    }

    // === GET: Buscar por email ===
    @GetMapping("/buscar/email")
    public ResponseEntity<Jugador> buscarPorEmail(@RequestParam String email) {
        // GET a /api/jugadores/buscar/email?email=carlos@mail.com
        // Busca un jugador por su email exacto
        return jugadorService.getJugadorByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar/email/contiene")
    public List<Jugador> buscarPorEmailContiene(@RequestParam String email) {
        // GET a /api/jugadores/buscar/email/contiene?email=carlos
        // Busca jugadores cuyo email contenga "carlos"
        return jugadorService.getJugadoresByEmailContaining(email);
    }

    // === GET: Por tipo de admin ===
    @GetMapping("/admin")
    public List<Jugador> obtenerAdmin() {
        // GET a /api/jugadores/admin
        // Jugadores que son administradores
        return jugadorService.getJugadoresAdmin();
    }

    @GetMapping("/no-admin")
    public List<Jugador> obtenerNoAdmin() {
        // GET a /api/jugadores/no-admin
        // Jugadores que NO son administradores
        return jugadorService.getJugadoresNoAdmin();
    }

    // === GET: Por fecha de nacimiento ===
    @GetMapping("/fecha-nac/before")
    public List<Jugador> obtenerFechaNacBefore(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        // GET a /api/jugadores/fecha-nac/before?fecha=2000-01-01
        // Jugadores nacidos ANTES de esa fecha
        return jugadorService.getJugadoresByFechaNacBefore(fecha);
    }

    @GetMapping("/fecha-nac/after")
    public List<Jugador> obtenerFechaNacAfter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        // GET a /api/jugadores/fecha-nac/after?fecha=2000-01-01
        // Jugadores nacidos DESPUÉS de esa fecha
        return jugadorService.getJugadoresByFechaNacAfter(fecha);
    }

    @GetMapping("/fecha-nac/between")
    public List<Jugador> obtenerFechaNacBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        // GET a /api/jugadores/fecha-nac/between?start=1990-01-01&end=2000-01-01
        // Jugadores nacidos entre esas fechas
        return jugadorService.getJugadoresByFechaNacBetween(start, end);
    }

    @GetMapping("/fecha-nac/null")
    public List<Jugador> obtenerSinFechaNac() {
        // GET a /api/jugadores/fecha-nac/null
        // Jugadores que no tienen fecha de nacimiento registrada
        return jugadorService.getJugadoresSinFechaNac();
    }

    @GetMapping("/fecha-nac/not-null")
    public List<Jugador> obtenerConFechaNac() {
        // GET a /api/jugadores/fecha-nac/not-null
        // Jugadores que SÍ tienen fecha de nacimiento
        return jugadorService.getJugadoresConFechaNac();
    }

    // === GET: Por sexo ===
    @GetMapping("/sexo/{sexo}")
    public List<Jugador> obtenerPorSexo(@PathVariable Sexo sexo) {
        // GET a /api/jugadores/sexo/MASCULINO
        // GET a /api/jugadores/sexo/FEMENINO
        // GET a /api/jugadores/sexo/OTRO
        // Filtra jugadores por sexo
        return jugadorService.getJugadoresBySexo(sexo);
    }

    // === POST: Login ===
    @PostMapping("/login")
    public ResponseEntity<Jugador> login(@RequestParam String nombreJug, @RequestParam String password) {
        // POST a /api/jugadores/login?nombreJug=Carlos&password=1234
        // Autentica al usuario con nombre y contraseña
        return jugadorService.login(nombreJug, password)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    // === POST: Crear ===
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Jugador crear(@RequestBody Jugador jugador) {
        // POST a /api/jugadores
        // Body: JSON con los datos del nuevo jugador
        // Guarda un jugador nuevo
        return jugadorService.saveJugador(jugador);
    }

    // === PUT: Actualizar ===
    @PutMapping("/{id}")
    public ResponseEntity<Jugador> actualizar(@PathVariable Integer id, @RequestBody Jugador jugador) {
        // PUT a /api/jugadores/3
        // Body: JSON con los datos actualizados
        // Actualiza el jugador con ID = 3
        return jugadorService.updateJugador(id, jugador)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === DELETE: Eliminar ===
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        // DELETE a /api/jugadores/3
        // Elimina el jugador con ID = 3
        if (jugadorService.getJugadorById(id).isPresent()) {
            jugadorService.deleteJugador(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // === GET: Estadísticas ===
    @GetMapping("/estadisticas/admin")
    public List<Object[]> obtenerEstadisticasPorAdmin() {
        // GET a /api/jugadores/estadisticas/admin
        // Cuántos admins y cuántos no admins hay
        return jugadorService.getCountByEsAdmin();
    }

    @GetMapping("/estadisticas/sexo")
    public List<Object[]> obtenerEstadisticasPorSexo() {
        // GET a /api/jugadores/estadisticas/sexo
        // Cuántos jugadores hay de cada sexo
        return jugadorService.getCountBySexo();
    }
}