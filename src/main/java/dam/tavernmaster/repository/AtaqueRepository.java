package dam.tavernmaster.repository;

import dam.tavernmaster.entity.Ataque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtaqueRepository extends JpaRepository<Ataque, Integer> {
    List<Ataque> findByPersonajeIdPer(Integer idPer);

    void deleteByPersonajeIdPer(Integer idPer);

    void deleteByPersonajeJugadorPadre(String jugadorPadre);
}
