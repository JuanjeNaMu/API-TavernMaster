package dam.tavernmaster.service;

import dam.tavernmaster.dto.AtaqueDTO;
import dam.tavernmaster.entity.Ataque;
import dam.tavernmaster.entity.Personaje;
import dam.tavernmaster.repository.AtaqueRepository;
import dam.tavernmaster.repository.PersonajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AtaqueService {

    @Autowired
    private AtaqueRepository ataqueRepository;

    @Autowired
    private PersonajeRepository personajeRepository;

    public List<AtaqueDTO> getAllAtaques() {
        return ataqueRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<AtaqueDTO> getAtaqueById(Integer idAtaque) {
        return ataqueRepository.findById(idAtaque).map(this::toDTO);
    }

    public List<AtaqueDTO> getAtaquesByPersonaje(Integer idPer) {
        return ataqueRepository.findByPersonajeIdPer(idPer).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public AtaqueDTO saveAtaque(AtaqueDTO dto) {
        if (dto.getIdPer() == null) {
            throw new IllegalArgumentException("id_per es obligatorio");
        }
        Personaje personaje = personajeRepository.findById(dto.getIdPer())
                .orElseThrow(() -> new IllegalArgumentException("No existe personaje con id_per=" + dto.getIdPer()));

        Ataque ataque = new Ataque();
        ataque.setPersonaje(personaje);
        ataque.setNombre(requireText(dto.getNombre(), "nombre"));
        ataque.setCaracteristica(requireText(dto.getCaracteristica(), "caracteristica"));
        ataque.setEsCompetente(Boolean.TRUE.equals(dto.getEsCompetente()));

        return toDTO(ataqueRepository.save(ataque));
    }

    @Transactional
    public Optional<AtaqueDTO> updateAtaque(Integer idAtaque, AtaqueDTO dto) {
        return ataqueRepository.findById(idAtaque).map(ataque -> {
            if (dto.getIdPer() != null && !dto.getIdPer().equals(ataque.getIdPer())) {
                Personaje personaje = personajeRepository.findById(dto.getIdPer())
                        .orElseThrow(() -> new IllegalArgumentException("No existe personaje con id_per=" + dto.getIdPer()));
                ataque.setPersonaje(personaje);
            }

            if (dto.getNombre() != null) {
                ataque.setNombre(requireText(dto.getNombre(), "nombre"));
            }
            if (dto.getCaracteristica() != null) {
                ataque.setCaracteristica(requireText(dto.getCaracteristica(), "caracteristica"));
            }
            if (dto.getEsCompetente() != null) {
                ataque.setEsCompetente(dto.getEsCompetente());
            }
            return toDTO(ataqueRepository.save(ataque));
        });
    }

    @Transactional
    public List<AtaqueDTO> replaceAtaquesByPersonaje(Integer idPer, List<AtaqueDTO> ataques) {
        Personaje personaje = personajeRepository.findById(idPer)
                .orElseThrow(() -> new IllegalArgumentException("No existe personaje con id_per=" + idPer));

        ataqueRepository.deleteByPersonajeIdPer(idPer);

        if (ataques == null || ataques.isEmpty()) {
            return List.of();
        }

        List<Ataque> nuevos = ataques.stream()
                .filter(dto -> dto.getNombre() != null && !dto.getNombre().isBlank())
                .map(dto -> {
                    Ataque ataque = new Ataque();
                    ataque.setPersonaje(personaje);
                    ataque.setNombre(requireText(dto.getNombre(), "nombre"));
                    ataque.setCaracteristica(requireText(dto.getCaracteristica(), "caracteristica"));
                    ataque.setEsCompetente(Boolean.TRUE.equals(dto.getEsCompetente()));
                    return ataque;
                })
                .collect(Collectors.toList());

        return ataqueRepository.saveAll(nuevos).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public void deleteAtaque(Integer idAtaque) {
        ataqueRepository.deleteById(idAtaque);
    }

    private String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " es obligatorio");
        }
        return value.trim();
    }

    private AtaqueDTO toDTO(Ataque ataque) {
        AtaqueDTO dto = new AtaqueDTO();
        dto.setIdAtaque(ataque.getIdAtaque());
        dto.setIdPer(ataque.getIdPer());
        dto.setNombre(ataque.getNombre());
        dto.setCaracteristica(ataque.getCaracteristica());
        dto.setEsCompetente(ataque.getEsCompetente());
        return dto;
    }
}
