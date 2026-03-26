package dam.tavernmaster.service;

import dam.tavernmaster.entity.Ficha;
import dam.tavernmaster.repository.FichaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FichaService {

    @Autowired
    private FichaRepository fichaRepository;

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

    public void deleteFicha(Integer id) {
        fichaRepository.deleteById(id);
    }
}
