package dam.tavernmaster.service;

import dam.tavernmaster.entity.Campana;
import dam.tavernmaster.repository.CampanaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CampanaService {

    @Autowired
    private CampanaRepository campanaRepository;

    // === GET: Todas ===
    public List<Campana> getAllCampanas() {
        return campanaRepository.findAll();
    }

    // === GET: Por ID ===
    public Optional<Campana> getCampanaById(Integer idCam) {
        return campanaRepository.findById(idCam);
    }

    // === GET: Con personajes ===
    public Optional<Campana> getCampanaWithPersonajes(Integer idCam) {
        return campanaRepository.findByIdWithPersonajes(idCam);
    }

    // === GET: Por título ===
    public List<Campana> getCampanasByTitulo(String titulo) {
        return campanaRepository.findByTituloContainingIgnoreCase(titulo);
    }

    // === GET: Por master ===
    public List<Campana> getCampanasByMaster(String master) {
        return campanaRepository.findByMasterContainingIgnoreCase(master);
    }

    // === GET: Por fecha (próxima sesión) ===
    public List<Campana> getCampanasByProximaSesionAfter(LocalDate fecha) {
        return campanaRepository.findByProximaSesionAfter(fecha);
    }

    public List<Campana> getCampanasByProximaSesionBetween(LocalDate start, LocalDate end) {
        return campanaRepository.findByProximaSesionBetween(start, end);
    }

    public List<Campana> getCampanasSinProximaSesion() {
        return campanaRepository.findByProximaSesionIsNull();
    }

    public List<Campana> getCampanasConProximaSesion() {
        return campanaRepository.findByProximaSesionIsNotNull();
    }

    // === GET: Por encuentros ===
    public List<Campana> getCampanasActivas() {
        return campanaRepository.findByEncuentrosGreaterThan(0);
    }

    public List<Campana> getCampanasFinalizadas() {
        return campanaRepository.findByEncuentros(0);
    }

    public List<Campana> getCampanasByEncuentros(Integer valor) {
        return campanaRepository.findByEncuentros(valor);
    }

    // === POST: Crear ===
    public Campana saveCampana(Campana campana) {
        return campanaRepository.save(campana);
    }

    // === PUT: Actualizar ===
    public Optional<Campana> updateCampana(Integer idCam, Campana campana) {
        return campanaRepository.findById(idCam).map(c -> {
            c.setTitulo(campana.getTitulo());
            c.setMaster(campana.getMaster());
            c.setProximaSesion(campana.getProximaSesion());
            c.setEncuentros(campana.getEncuentros());
            return campanaRepository.save(c);
        });
    }

    // === PATCH: Finalizar ===
    public Optional<Campana> finalizarCampana(Integer idCam) {
        return campanaRepository.findById(idCam).map(c -> {
            c.setEncuentros(0);
            return campanaRepository.save(c);
        });
    }

    // === DELETE: Eliminar ===
    public void deleteCampana(Integer idCam) {
        campanaRepository.deleteById(idCam);
    }

    // === GET: Estadísticas ===
    public List<Object[]> getCampanasCountByMaster() {
        return campanaRepository.countCampanasByMaster();
    }
}