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

    // CAPA REPOSITORY
    // Aquí hablo directamente con la base de datos para Jugadores
    // JpaRepository ya me da save, findById, findAll, delete, etc.

    // === BÚSQUEDAS POR TEXTO ===
    Optional<Jugador> findByNombreJugIgnoreCase(String nombreJug);
    // Busca un jugador por nombre exacto (sin importar mayúsculas)

    List<Jugador> findByNombreJugContainingIgnoreCase(String nombreJug);
    // Busca jugadores que contengan ese texto en el nombre

    Optional<Jugador> findByEmailIgnoreCase(String email);
    // Busca un jugador por email exacto

    List<Jugador> findByEmailContainingIgnoreCase(String email);
    // Busca jugadores que contengan ese texto en el email

    // === BÚSQUEDAS POR ADMIN ===
    List<Jugador> findByEsAdminTrue();
    // Todos los administradores

    List<Jugador> findByEsAdminFalse();
    // Todos los jugadores normales (no admins)

    // === BÚSQUEDAS POR FECHA ===
    List<Jugador> findByFechaNacBefore(LocalDate fecha);
    // Nacidos antes de X fecha

    List<Jugador> findByFechaNacAfter(LocalDate fecha);
    // Nacidos después de X fecha

    List<Jugador> findByFechaNacBetween(LocalDate start, LocalDate end);
    // Nacidos entre dos fechas

    List<Jugador> findByFechaNacIsNull();
    // Sin fecha de nacimiento registrada

    List<Jugador> findByFechaNacIsNotNull();
    // Con fecha de nacimiento registrada

    // === BÚSQUEDAS POR SEXO ===
    List<Jugador> findBySexo(Sexo sexo);
    // Filtra por sexo (Hombre, Mujer, Otro, Vacio)

    // === LOGIN (autenticación) ===
    Optional<Jugador> findByNombreJugIgnoreCaseAndPassword(String nombreJug, String password);
    // Busca por nombre y contraseña (la contraseña YA VIENE ENCRIPTADA aquí)

    // === JOIN FETCH con personajes ===
    @Query("SELECT j FROM Jugador j LEFT JOIN FETCH j.personajes WHERE j.id = :id")
    Optional<Jugador> findByIdWithPersonajes(@Param("id") Integer id);
    // Trae al jugador con TODOS sus personajes en la misma consulta
    // Si no uso esto, personajes viene vacío (Lazy) y da error

    // === ESTADÍSTICAS ===
    @Query("SELECT j.esAdmin, COUNT(j) FROM Jugador j GROUP BY j.esAdmin")
    List<Object[]> countByEsAdmin();
    // Devuelve [true, total_admins] y [false, total_no_admins]

    @Query("SELECT j.sexo, COUNT(j) FROM Jugador j WHERE j.sexo IS NOT NULL GROUP BY j.sexo")
    List<Object[]> countBySexo();
    // Devuelve [Hombre, total], [Mujer, total], [Otro, total], [Vacio, total]

    // === LOGIN CON VALIDACIÓN (para consultas personalizadas) ===
    @Query("SELECT j FROM Jugador j WHERE LOWER(j.nombreJug) = LOWER(:nombreJug) AND j.password = :password")
    Optional<Jugador> login(@Param("nombreJug") String nombreJug, @Param("password") String password);
    // Otra forma de hacer login, con query manual

}

// NOTAS MENTALES:
// - findByNombreJugAndPassword asume que la password ya está encriptada
// - El login compara el hash encriptado, no texto plano
// - JOIN FETCH es obligatorio si quiero acceder a personajes fuera del contexto JPA
// - Las consultas con GROUP BY devuelven Object[], luego las proceso en Service