package dam.saruman.controller;

import dam.saruman.entity.Enemigo;
import dam.saruman.service.EnemigoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//ESTO PERMITE GESTIONAR LAS PETICIONES DEVOLEVIENDO SIEMPRE COMO RESPUESTA UN JSON

@RestController
@RequestMapping("/api")
public class EnemigoController{
    @Autowired
    private EnemigoService enemigoService;

    @GetMapping("/enemigos")
    public List<Enemigo> obtenerEnemigos(){
        return enemigoService.obtenerTodos();
    }

    @PostMapping("/enemigos")
    public Enemigo crearEnemigo(@RequestBody Enemigo enemigo){
        return enemigoService.guardar(enemigo);
    }

    @PutMapping("/enemigos/{id}")
    public ResponseEntity<Enemigo> actualizarEnemigo(@PathVariable Long id, @RequestBody Enemigo enemigo){
        Enemigo enemigoActualizado = enemigoService.actualizar(id, enemigo);

        if(enemigoActualizado != null){
            return ResponseEntity.ok(enemigoActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/enemigos/{id}")
    public ResponseEntity<Void> eliminarEnemigo(@PathVariable Long id){
        boolean eliminado = enemigoService.eliminar(id);

        if(eliminado){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}