package dam.tavernmaster.repository;

import dam.tavernmaster.entity.Jugador;
import dam.tavernmaster.entity.Sexo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Integer> {

    // === BÚSQUEDAS POR TEXTO ===
    Optional<Jugador> findByNombreJugIgnoreCase(String nombreJug);
    List<Jugador> findByNombreJugContainingIgnoreCase(String nombreJug);
    Optional<Jugador> findByEmailIgnoreCase(String email);
    List<Jugador> findByEmailContainingIgnoreCase(String email);

    // === BÚSQUEDAS POR ADMIN ===
    List<Jugador> findByEsAdminTrue();
    List<Jugador> findByEsAdminFalse();

    // === BÚSQUEDAS POR FECHA ===
    List<Jugador> findByFechaNacBefore(LocalDate fecha);
    List<Jugador> findByFechaNacAfter(LocalDate fecha);
    List<Jugador> findByFechaNacBetween(LocalDate start, LocalDate end);
    List<Jugador> findByFechaNacIsNull();
    List<Jugador> findByFechaNacIsNotNull();

    // === BÚSQUEDAS POR SEXO ===
    List<Jugador> findBySexo(Sexo sexo);

    // === LOGIN (autenticación) ===
    Optional<Jugador> findByNombreJugIgnoreCaseAndPassword(String nombreJug, String password);

    // === JOIN FETCH con personajes ===
    @Query("SELECT j FROM Jugador j LEFT JOIN FETCH j.personajes WHERE j.id = :id")
    Optional<Jugador> findByIdWithPersonajes(@Param("id") Integer id);

    // === ESTADÍSTICAS ===
    @Query("SELECT j.esAdmin, COUNT(j) FROM Jugador j GROUP BY j.esAdmin")
    List<Object[]> countByEsAdmin();

    @Query("SELECT j.sexo, COUNT(j) FROM Jugador j WHERE j.sexo IS NOT NULL GROUP BY j.sexo")
    List<Object[]> countBySexo();

    // === LOGIN CON VALIDACIÓN (para consultas personalizadas) ===
    @Query("SELECT j FROM Jugador j WHERE LOWER(j.nombreJug) = LOWER(:nombreJug) AND j.password = :password")
    Optional<Jugador> login(@Param("nombreJug") String nombreJug, @Param("password") String password);
}