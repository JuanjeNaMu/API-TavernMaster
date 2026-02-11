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

    @Autowired
    private JugadorRepository jugadorRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // === CRUD BÁSICO ===
    public Jugador saveJugador(Jugador jugador) {
        // 👇 AÑADE ESTAS 3 LÍNEAS AQUÍ
        // Si la contraseña NO parece un hash de bcrypt, la hasheamos
        if (jugador.getPassword() != null && !jugador.getPassword().startsWith("$2")) {
            jugador.setPassword(passwordEncoder.encode(jugador.getPassword()));
        }
        return jugadorRepository.save(jugador);
    }

    public List<Jugador> getAllJugadores() {
        return jugadorRepository.findAll();
    }

    public Optional<Jugador> getJugadorById(Integer id) {
        return jugadorRepository.findById(id);
    }

    public void deleteJugador(Integer id) {
        jugadorRepository.deleteById(id);
    }

    // === RELACIONES ===
    public Optional<Jugador> getJugadorWithPersonajes(Integer id) {
        return jugadorRepository.findByIdWithPersonajes(id);
    }

    // === BÚSQUEDAS POR TEXTO ===
    public Optional<Jugador> getJugadorByNombreJug(String nombreJug) {
        return jugadorRepository.findByNombreJugIgnoreCase(nombreJug);
    }

    public List<Jugador> getJugadoresByNombreJugContaining(String nombreJug) {
        return jugadorRepository.findByNombreJugContainingIgnoreCase(nombreJug);
    }

    public Optional<Jugador> getJugadorByEmail(String email) {
        return jugadorRepository.findByEmailIgnoreCase(email);
    }

    public List<Jugador> getJugadoresByEmailContaining(String email) {
        return jugadorRepository.findByEmailContainingIgnoreCase(email);
    }

    // === BÚSQUEDAS POR ADMIN ===
    public List<Jugador> getJugadoresAdmin() {
        return jugadorRepository.findByEsAdminTrue();
    }

    public List<Jugador> getJugadoresNoAdmin() {
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
        return jugadorRepository.login(nombreJug, password);
    }

    // === ACTUALIZACIONES ===
    public Optional<Jugador> updateJugador(Integer id, Jugador jugadorDetails) {
        return jugadorRepository.findById(id).map(jugador -> {
            if (jugadorDetails.getNombreJug() != null)
                jugador.setNombreJug(jugadorDetails.getNombreJug());

            // 👇 AÑADE ESTAS 3 LÍNEAS PARA LA CONTRASEÑA
            if (jugadorDetails.getPassword() != null) {
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
        return jugadorRepository.countByEsAdmin();
    }

    public List<Object[]> getCountBySexo() {
        return jugadorRepository.countBySexo();
    }
}