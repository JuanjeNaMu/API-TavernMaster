package dam.tavernmaster.service;

import dam.tavernmaster.entity.Campana;
import dam.tavernmaster.entity.Personaje;
import dam.tavernmaster.repository.PersonajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PersonajeService {

    // CAPA SERVICE
    // Aquí va la lógica de negocio de Personajes
    // Es la más sencilla porque solo gestiona personajes y su asignación a campañas

    @Autowired
    private PersonajeRepository personajeRepository;

    // === GET: Todos ===
    public List<Personaje> getAllPersonajes() {
        // Devuelve todos los personajes
        return personajeRepository.findAll();
    }

    // === GET: Por ID ===
    public Optional<Personaje> getPersonajeById(Integer idPer) {
        // Busca un personaje por su ID
        return personajeRepository.findById(idPer);
    }

    // === GET: Por jugador padre ===
    public List<Personaje> getPersonajesByJugadorPadre(String jugadorPadre) {
        // Busca todos los personajes de un jugador específico
        return personajeRepository.findByJugadorPadre(jugadorPadre);
    }

    // === GET: Por campaña ID ===
    public List<Personaje> getPersonajesByCampanaId(Integer idCam) {
        // Busca todos los personajes que están en una campaña
        return personajeRepository.findByCampanaIdCam(idCam);
    }

    // === GET: Por nivel mínimo ===
    public List<Personaje> getPersonajesByNivelMinimo(Integer nivel) {
        // Filtra personajes por nivel mínimo
        return personajeRepository.findByNivelGreaterThanEqual(nivel);
    }

    // === POST: Crear ===
    public Personaje savePersonaje(Personaje personaje) {
        // Guarda un personaje nuevo
        return personajeRepository.save(personaje);
    }

    // === PUT: Actualizar ===
    public Optional<Personaje> updatePersonaje(Integer idPer, Personaje personaje) {
        // Actualiza los datos de un personaje existente
        return personajeRepository.findById(idPer).map(p -> {
            p.setNombrePer(personaje.getNombrePer());
            p.setNivel(personaje.getNivel());
            p.setJugadorPadre(personaje.getJugadorPadre());
            p.setCampana(personaje.getCampana());
            p.setImagenBase64(personaje.getImagenBase64());
            return personajeRepository.save(p);
        });
    }

    // === DELETE: Eliminar ===
    public void deletePersonaje(Integer idPer) {
        // Borra un personaje de la BD
        personajeRepository.deleteById(idPer);
    }

    // === PATCH: Asignar campaña ===
    public Optional<Personaje> asignarCampana(Integer idPer, Integer idCam) {
        // Asigna un personaje a una campaña
        // Solo necesita el ID de la campaña, no el objeto completo
        return personajeRepository.findById(idPer).map(personaje -> {
            Campana campana = new Campana();
            campana.setIdCam(idCam);
            personaje.setCampana(campana);
            return personajeRepository.save(personaje);
        });
    }
}

// NOTAS MENTALES:
// - Es el service más simple de los tres (solo gestiona personajes)
// - asignarCampana: creo un objeto Campana solo con el ID para asignarlo
//   No necesito buscar la campaña entera, con el ID basta
// - Si la campaña no existe, igualmente se asigna y da error de FK en BD
//   Habría que mejorarlo con validación (pero para el ejemplo vale)