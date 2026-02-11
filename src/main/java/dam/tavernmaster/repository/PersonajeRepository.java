package dam.tavernmaster.repository;

import dam.tavernmaster.entity.Personaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PersonajeRepository extends JpaRepository<Personaje, Integer> {

    // Buscar por jugador padre
    List<Personaje> findByJugadorPadre(String jugadorPadre);

    // Buscar por ID de campaña (CORREGIDO)
    List<Personaje> findByCampanaIdCam(Integer idCam);

    // Buscar por nivel mínimo
    List<Personaje> findByNivelGreaterThanEqual(Integer nivel);
}