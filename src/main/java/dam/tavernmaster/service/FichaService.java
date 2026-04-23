package dam.tavernmaster.service;

import dam.tavernmaster.dto.AtaqueDTO;
import dam.tavernmaster.dto.FichaConAtaquesDTO;
import dam.tavernmaster.entity.Ataque;
import dam.tavernmaster.entity.Ficha;
import dam.tavernmaster.repository.AtaqueRepository;
import dam.tavernmaster.repository.FichaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FichaService {

    @Autowired
    private FichaRepository fichaRepository;

    @Autowired
    private AtaqueRepository ataqueRepository;

    public List<Ficha> getAllFichas() {
        return fichaRepository.findAll();
    }

    public Optional<Ficha> getFichaById(Integer id) {
        return fichaRepository.findById(id);
    }

    public Ficha saveFicha(Ficha ficha) {
        return fichaRepository.save(ficha);
    }

    public Optional<Ficha> updateFicha(Integer id, Ficha fichaData) {
        return fichaRepository.findById(id).map(ficha -> {
            if (fichaData.getClase() != null) ficha.setClase(fichaData.getClase());
            if (fichaData.getFuerza() != null) ficha.setFuerza(fichaData.getFuerza());
            if (fichaData.getDestreza() != null) ficha.setDestreza(fichaData.getDestreza());
            if (fichaData.getConstitucion() != null) ficha.setConstitucion(fichaData.getConstitucion());
            if (fichaData.getInteligencia() != null) ficha.setInteligencia(fichaData.getInteligencia());
            if (fichaData.getSabiduria() != null) ficha.setSabiduria(fichaData.getSabiduria());
            if (fichaData.getCarisma() != null) ficha.setCarisma(fichaData.getCarisma());
            return fichaRepository.save(ficha);
        });
    }

    public List<FichaConAtaquesDTO> getFichasConAtaques() {
        return fichaRepository.findAll().stream()
                .map(this::toFichaConAtaquesDTO)
                .collect(Collectors.toList());
    }

    public void deleteFicha(Integer id) {
        fichaRepository.deleteById(id);
    }

    private FichaConAtaquesDTO toFichaConAtaquesDTO(Ficha ficha) {
        FichaConAtaquesDTO dto = new FichaConAtaquesDTO();
        dto.setIdFicha(ficha.getIdFicha());
        dto.setIdPer(ficha.getIdFicha());
        dto.setClase(ficha.getClase());
        dto.setFuerza(ficha.getFuerza());
        dto.setDestreza(ficha.getDestreza());
        dto.setConstitucion(ficha.getConstitucion());
        dto.setInteligencia(ficha.getInteligencia());
        dto.setSabiduria(ficha.getSabiduria());
        dto.setCarisma(ficha.getCarisma());

        if (ficha.getIdFicha() == null) {
            dto.setAtaques(List.of());
            return dto;
        }

        List<AtaqueDTO> ataques = ataqueRepository.findByPersonajeIdPer(ficha.getIdFicha()).stream()
                .map(this::toAtaqueDTO)
                .collect(Collectors.toList());
        dto.setAtaques(ataques);
        return dto;
    }

    private AtaqueDTO toAtaqueDTO(Ataque ataque) {
        AtaqueDTO dto = new AtaqueDTO();
        dto.setIdAtaque(ataque.getIdAtaque());
        dto.setIdPer(ataque.getIdPer());
        dto.setNombre(ataque.getNombre());
        dto.setCaracteristica(ataque.getCaracteristica());
        dto.setEsCompetente(ataque.getEsCompetente());
        return dto;
    }
}
