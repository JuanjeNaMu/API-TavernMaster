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

    // CAPA SERVICE
    // Aquí va la lógica de negocio de Campañas
    // Recibe peticiones del Controller y llama al Repository

    @Autowired
    private CampanaRepository campanaRepository;

    // === GET: Todas ===
    public List<Campana> getAllCampanas() {
        // Devuelve todas las campañas
        return campanaRepository.findAll();
    }

    // === GET: Por ID ===
    public Optional<Campana> getCampanaById(Integer idCam) {
        // Busca una campaña por su ID
        // Optional puede venir vacío si no existe
        return campanaRepository.findById(idCam);
    }

    // === GET: Con personajes ===
    public Optional<Campana> getCampanaWithPersonajes(Integer idCam) {
        // Busca campaña y OBLIGA a traer los personajes en la misma consulta
        // Si no uso esto, al intentar acceder a personajes fuera del repository da error
        return campanaRepository.findByIdWithPersonajes(idCam);
    }

    // === GET: Por título ===
    public List<Campana> getCampanasByTitulo(String titulo) {
        // Busca campañas que contengan ese texto en el título
        return campanaRepository.findByTituloContainingIgnoreCase(titulo);
    }

    // === GET: Por master ===
    public List<Campana> getCampanasByMaster(String master) {
        // Busca campañas de un master específico
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
        // Campañas sin fecha programada
        return campanaRepository.findByProximaSesionIsNull();
    }

    public List<Campana> getCampanasConProximaSesion() {
        // Campañas que ya tienen fecha
        return campanaRepository.findByProximaSesionIsNotNull();
    }

    // === GET: Por encuentros ===
    public List<Campana> getCampanasByEncuentros(Integer valor) {
        // Campañas con exactamente X encuentros
        return campanaRepository.findByEncuentros(valor);
    }

    // === POST: Crear ===
    public Campana saveCampana(Campana campana) {
        // Guarda una campaña nueva
        // El ID debe venir null para que la BD lo genere
        return campanaRepository.save(campana);
    }

    // === PUT: Actualizar ===
    public Optional<Campana> updateCampana(Integer idCam, Campana campana) {
        // Busca la campaña por ID y actualiza sus campos
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
        // Marca una campaña como finalizada
        // Pone los encuentros a 0 (no se borra, solo se desactiva)
        return campanaRepository.findById(idCam).map(c -> {
            c.setEncuentros(0);
            return campanaRepository.save(c);
        });
    }

    // === DELETE: Eliminar ===
    public void deleteCampana(Integer idCam) {
        // Borra la campaña de la BD
        campanaRepository.deleteById(idCam);
    }

    // === GET: Estadísticas ===
    public List<Object[]> getCampanasCountByMaster() {
        // Devuelve cuántas campañas ha hecho cada master
        // El repository devuelve Object[], habría que mapearlo si quiero un DTO
        return campanaRepository.countCampanasByMaster();
    }
}

// NOTAS MENTALES:
// - Service es el que hace el trabajo, Controller solo recibe y responde
// - Los Optional evitan null pointers
// - En updateCampana: si no existe el ID, devuelve Optional.empty() y el Controller responde 404
// - finalizarCampana: no borra, solo resetea encuentros (campaña terminada)