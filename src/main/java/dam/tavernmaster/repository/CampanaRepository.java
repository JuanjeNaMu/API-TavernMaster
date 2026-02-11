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

    // === Buscar por título ===
    List<Campana> findByTituloContainingIgnoreCase(String titulo);

    // === Buscar por master ===
    List<Campana> findByMasterContainingIgnoreCase(String master);

    // === Buscar por próxima sesión ===
    List<Campana> findByProximaSesionAfter(LocalDate fecha);
    List<Campana> findByProximaSesionBetween(LocalDate start, LocalDate end);
    List<Campana> findByProximaSesionIsNull();
    List<Campana> findByProximaSesionIsNotNull();

    // === Buscar por encuentros ===
    List<Campana> findByEncuentros(Integer encuentros);
    List<Campana> findByEncuentrosGreaterThan(Integer encuentros);

    // === Con personajes (JOIN FETCH) ===
    @Query("SELECT c FROM Campana c LEFT JOIN FETCH c.personajes WHERE c.idCam = :idCam")
    Optional<Campana> findByIdWithPersonajes(@Param("idCam") Integer idCam);

    // === Estadísticas por master ===
    @Query("SELECT c.master, COUNT(c) FROM Campana c GROUP BY c.master")
    List<Object[]> countCampanasByMaster();
}