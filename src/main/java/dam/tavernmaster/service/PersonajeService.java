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

    @Autowired
    private PersonajeRepository personajeRepository;

    // === GET: Todos ===
    public List<Personaje> getAllPersonajes() {
        return personajeRepository.findAll();
    }

    // === GET: Por ID ===
    public Optional<Personaje> getPersonajeById(Integer idPer) {
        return personajeRepository.findById(idPer);
    }

    // === GET: Por jugador padre ===
    public List<Personaje> getPersonajesByJugadorPadre(String jugadorPadre) {
        return personajeRepository.findByJugadorPadre(jugadorPadre);
    }

    // === GET: Por campaña ID ===
    public List<Personaje> getPersonajesByCampanaId(Integer idCam) {
        return personajeRepository.findByCampanaIdCam(idCam);
    }

    // === GET: Por nivel mínimo ===
    public List<Personaje> getPersonajesByNivelMinimo(Integer nivel) {
        return personajeRepository.findByNivelGreaterThanEqual(nivel);
    }

    // === POST: Crear ===
    public Personaje savePersonaje(Personaje personaje) {
        return personajeRepository.save(personaje);
    }

    // === PUT: Actualizar ===
    public Optional<Personaje> updatePersonaje(Integer idPer, Personaje personaje) {
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
        personajeRepository.deleteById(idPer);
    }

    // === PATCH: Asignar campaña ===
    public Optional<Personaje> asignarCampana(Integer idPer, Integer idCam) {
        return personajeRepository.findById(idPer).map(personaje -> {
            Campana campana = new Campana();
            campana.setIdCam(idCam);
            personaje.setCampana(campana);
            return personajeRepository.save(personaje);
        });
    }
}