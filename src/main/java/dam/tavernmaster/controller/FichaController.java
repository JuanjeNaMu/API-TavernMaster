package dam.tavernmaster.controller;

import dam.tavernmaster.entity.Ficha;
import dam.tavernmaster.service.FichaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fichas")
public class FichaController {

    @Autowired
    private FichaService fichaService;

    @GetMapping
    public List<Ficha> obtenerTodas() {
        return fichaService.getAllFichas();
    }

    @GetMapping("/{idFicha}")
    public ResponseEntity<Ficha> obtenerPorId(@PathVariable Integer idFicha) {
        return fichaService.getFichaById(idFicha)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Ficha crear(@RequestBody Ficha ficha) {
        return fichaService.saveFicha(ficha);
    }

    @PutMapping("/{idFicha}")
    public ResponseEntity<Ficha> actualizar(@PathVariable Integer idFicha, @RequestBody Ficha ficha) {
        return fichaService.updateFicha(idFicha, ficha)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{idFicha}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idFicha) {
        if (fichaService.getFichaById(idFicha).isPresent()) {
            fichaService.deleteFicha(idFicha);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
