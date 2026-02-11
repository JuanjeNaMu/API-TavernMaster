package dam.tavernmaster.service;

import dam.tavernmaster.entity.Jugador;
import dam.tavernmaster.entity.Sexo;
import dam.tavernmaster.repository.JugadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class JugadorService {

    // CAPA SERVICE
    // Aquí va la lógica de negocio de Jugadores
    // Esta es la clase MÁS IMPORTANTE porque gestiona la autenticación y el encriptado

    @Autowired
    private JugadorRepository jugadorRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    // Inyecto el encriptador que configuré en SecurityConfig

    // === CRUD BÁSICO ===
    public Jugador saveJugador(Jugador jugador) {
        // GUARDAR JUGADOR NUEVO
        // Si la contraseña NO parece un hash de bcrypt, la hasheamos
        if (jugador.getPassword() != null && !jugador.getPassword().startsWith("$2")) {
            jugador.setPassword(passwordEncoder.encode(jugador.getPassword()));
        }
        return jugadorRepository.save(jugador);
    }

    public List<Jugador> getAllJugadores() {
        // Devuelve todos los jugadores
        return jugadorRepository.findAll();
    }

    public Optional<Jugador> getJugadorById(Integer id) {
        // Busca un jugador por ID
        return jugadorRepository.findById(id);
    }

    public void deleteJugador(Integer id) {
        // Borra un jugador
        jugadorRepository.deleteById(id);
    }

    // === RELACIONES ===
    public Optional<Jugador> getJugadorWithPersonajes(Integer id) {
        // Trae jugador con todos sus personajes
        return jugadorRepository.findByIdWithPersonajes(id);
    }

    // === BÚSQUEDAS POR TEXTO ===
    public Optional<Jugador> getJugadorByNombreJug(String nombreJug) {
        // Busca por nombre exacto
        return jugadorRepository.findByNombreJugIgnoreCase(nombreJug);
    }

    public List<Jugador> getJugadoresByNombreJugContaining(String nombreJug) {
        // Busca por nombre parcial
        return jugadorRepository.findByNombreJugContainingIgnoreCase(nombreJug);
    }

    public Optional<Jugador> getJugadorByEmail(String email) {
        // Busca por email exacto
        return jugadorRepository.findByEmailIgnoreCase(email);
    }

    public List<Jugador> getJugadoresByEmailContaining(String email) {
        // Busca por email parcial
        return jugadorRepository.findByEmailContainingIgnoreCase(email);
    }

    // === BÚSQUEDAS POR ADMIN ===
    public List<Jugador> getJugadoresAdmin() {
        // Solo admins
        return jugadorRepository.findByEsAdminTrue();
    }

    public List<Jugador> getJugadoresNoAdmin() {
        // Solo jugadores normales
        return jugadorRepository.findByEsAdminFalse();
    }

    // === BÚSQUEDAS POR FECHA ===
    public List<Jugador> getJugadoresByFechaNacBefore(LocalDate fecha) {
        return jugadorRepository.findByFechaNacBefore(fecha);
    }

    public List<Jugador> getJugadoresByFechaNacAfter(LocalDate fecha) {
        return jugadorRepository.findByFechaNacAfter(fecha);
    }

    public List<Jugador> getJugadoresByFechaNacBetween(LocalDate start, LocalDate end) {
        return jugadorRepository.findByFechaNacBetween(start, end);
    }

    public List<Jugador> getJugadoresSinFechaNac() {
        return jugadorRepository.findByFechaNacIsNull();
    }

    public List<Jugador> getJugadoresConFechaNac() {
        return jugadorRepository.findByFechaNacIsNotNull();
    }

    // === BÚSQUEDAS POR SEXO ===
    public List<Jugador> getJugadoresBySexo(Sexo sexo) {
        return jugadorRepository.findBySexo(sexo);
    }

    // === AUTENTICACIÓN ===
    public Optional<Jugador> login(String nombreJug, String password) {
        // LOGIN: Compara nombre y contraseña ENCRIPTADA
        return jugadorRepository.login(nombreJug, password);
    }

    // === ACTUALIZACIONES ===
    public Optional<Jugador> updateJugador(Integer id, Jugador jugadorDetails) {
        // Actualiza un jugador existente
        return jugadorRepository.findById(id).map(jugador -> {
            if (jugadorDetails.getNombreJug() != null)
                jugador.setNombreJug(jugadorDetails.getNombreJug());

            // ACTUALIZAR CONTRASEÑA
            if (jugadorDetails.getPassword() != null) {
                // Si ya es un hash, lo guardo tal cual
                // Si no, lo encripto antes de guardar
                if (!jugadorDetails.getPassword().startsWith("$2")) {
                    jugador.setPassword(passwordEncoder.encode(jugadorDetails.getPassword()));
                } else {
                    jugador.setPassword(jugadorDetails.getPassword());
                }
            }

            if (jugadorDetails.getEmail() != null)
                jugador.setEmail(jugadorDetails.getEmail());
            if (jugadorDetails.getEsAdmin() != null)
                jugador.setEsAdmin(jugadorDetails.getEsAdmin());
            if (jugadorDetails.getFechaNac() != null)
                jugador.setFechaNac(jugadorDetails.getFechaNac());
            if (jugadorDetails.getSexo() != null)
                jugador.setSexo(jugadorDetails.getSexo());
            return jugadorRepository.save(jugador);
        });
    }

    // === ESTADÍSTICAS ===
    public List<Object[]> getCountByEsAdmin() {
        // Cuántos admins y cuántos no
        return jugadorRepository.countByEsAdmin();
    }

    public List<Object[]> getCountBySexo() {
        // Cuántos de cada sexo
        return jugadorRepository.countBySexo();
    }
}

// NOTAS MENTALES (MUY IMPORTANTES):
// - La contraseña NUNCA se guarda en texto plano, SIEMPRE encriptada
// - El login compara el hash de la BD con el hash de lo que escribió el usuario
// - startsWith("$2") es la forma de saber si una contraseña YA está encriptada
// - Si no hago esa comprobación, al actualizar puedo encriptar algo que ya está encriptado
// - @Transactional asegura que todas las operaciones se hagan en una misma transacción