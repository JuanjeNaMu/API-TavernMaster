package dam.tavernmaster.repository;

import dam.tavernmaster.entity.Personaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PersonajeRepository extends JpaRepository<Personaje, Integer> {

    // CAPA REPOSITORY
    // Aquí hablo directamente con la base de datos para Personajes
    // JpaRepository ya me da save, findById, findAll, delete, etc.

    // Buscar por jugador padre
    List<Personaje> findByJugadorPadre(String jugadorPadre);
    // SELECT * FROM personaje WHERE jugador_padre = :jugadorPadre
    // Devuelve todos los personajes de un jugador específico

    // Buscar por ID de campaña (CORREGIDO)
    List<Personaje> findByCampanaIdCam(Integer idCam);
    // SELECT * FROM personaje WHERE id_cam = :idCam
    // Devuelve todos los personajes que pertenecen a una campaña
    // El método "navega" desde Personaje -> Campana -> idCam

    // Buscar por nivel mínimo
    List<Personaje> findByNivelGreaterThanEqual(Integer nivel);
    // SELECT * FROM personaje WHERE nivel >= :nivel
    // Personajes con nivel igual o superior al indicado

}

// NOTAS MENTALES:
// - Es la capa más simple de las tres (menos consultas personalizadas)
// - findByCampanaIdCam es la forma correcta, antes estaba mal
// - No necesito JOIN FETCH aquí porque Personaje NO tiene colecciones (tiene ManyToOne)
// - Los métodos básicos ya los heredo de JpaRepository