package dam.tavernmaster.repository;

import dam.tavernmaster.entity.Campana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CampanaRepository extends JpaRepository<Campana, Integer> {

    // CAPA REPOSITORY
    // Aquí hablo directamente con la base de datos
    // JpaRepository ya me da los métodos básicos: save, findById, findAll, delete, etc.
    // Aquí solo añado las consultas personalizadas

    // === Buscar por título ===
    List<Campana> findByTituloContainingIgnoreCase(String titulo);
    // SELECT * FROM campana WHERE titulo LIKE %:titulo% (sin importar mayúsculas)

    // === Buscar por master ===
    List<Campana> findByMasterContainingIgnoreCase(String master);
    // SELECT * FROM campana WHERE master LIKE %:master%

    // === Buscar por próxima sesión ===
    List<Campana> findByProximaSesionAfter(LocalDate fecha);
    // SELECT * FROM campana WHERE proxima_sesion > :fecha

    List<Campana> findByProximaSesionBetween(LocalDate start, LocalDate end);
    // SELECT * FROM campana WHERE proxima_sesion BETWEEN :start AND :end

    List<Campana> findByProximaSesionIsNull();
    // SELECT * FROM campana WHERE proxima_sesion IS NULL

    List<Campana> findByProximaSesionIsNotNull();
    // SELECT * FROM campana WHERE proxima_sesion IS NOT NULL

    // === Buscar por encuentros ===
    List<Campana> findByEncuentros(Integer encuentros);
    // SELECT * FROM campana WHERE encuentros = :encuentros

    List<Campana> findByEncuentrosGreaterThan(Integer encuentros);
    // SELECT * FROM campana WHERE encuentros > :encuentros

    // === Con personajes (JOIN FETCH) ===
    @Query("SELECT c FROM Campana c LEFT JOIN FETCH c.personajes WHERE c.idCam = :idCam")
    Optional<Campana> findByIdWithPersonajes(@Param("idCam") Integer idCam);
    // IMPORTANTE: Con JOIN FETCH obligo a traer los personajes en la misma consulta
    // Si no uso esto, personajes viene vacío (Lazy) y da error LazyInitializationException

    // === Estadísticas por master ===
    @Query("SELECT c.master, COUNT(c) FROM Campana c GROUP BY c.master")
    List<Object[]> countCampanasByMaster();
    // Devuelve una lista de arrays [master, total_campañas]

}

// NOTAS MENTALES:
// - No implemento nada, Spring Data JPA lo hace solo
// - Los nombres de los métodos tienen que seguir el patrón findBy... o uso @Query
// - ContainingIgnoreCase = LIKE %...% sin importar mayúsculas
// - JOIN FETCH es CLAVE para relaciones OneToMany (evita el error Lazy)
// - Cuando devuelvo Object[] tengo que mapearlo luego en el Service