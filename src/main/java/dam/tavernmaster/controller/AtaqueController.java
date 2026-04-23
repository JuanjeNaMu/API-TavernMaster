package dam.tavernmaster.controller;

import dam.tavernmaster.dto.AtaqueDTO;
import dam.tavernmaster.service.AtaqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ataques")
public class AtaqueController {

    @Autowired
    private AtaqueService ataqueService;

    @GetMapping
    public List<AtaqueDTO> obtenerTodos() {
        return ataqueService.getAllAtaques();
    }

    @GetMapping("/{idAtaque}")
    public ResponseEntity<AtaqueDTO> obtenerPorId(@PathVariable Integer idAtaque) {
        return ataqueService.getAtaqueById(idAtaque)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/personaje/{idPer}")
    public List<AtaqueDTO> obtenerPorPersonaje(@PathVariable Integer idPer) {
        return ataqueService.getAtaquesByPersonaje(idPer);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AtaqueDTO crear(@RequestBody AtaqueDTO ataqueDTO) {
        return ataqueService.saveAtaque(ataqueDTO);
    }

    @PutMapping("/{idAtaque}")
    public ResponseEntity<AtaqueDTO> actualizar(@PathVariable Integer idAtaque, @RequestBody AtaqueDTO ataqueDTO) {
        return ataqueService.updateAtaque(idAtaque, ataqueDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/personaje/{idPer}")
    public List<AtaqueDTO> reemplazarAtaquesDePersonaje(@PathVariable Integer idPer,
                                                        @RequestBody List<AtaqueDTO> ataques) {
        return ataqueService.replaceAtaquesByPersonaje(idPer, ataques);
    }

    @DeleteMapping("/{idAtaque}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idAtaque) {
        if (ataqueService.getAtaqueById(idAtaque).isPresent()) {
            ataqueService.deleteAtaque(idAtaque);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> manejarBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
}
