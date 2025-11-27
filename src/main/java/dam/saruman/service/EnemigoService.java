package dam.saruman.service;

import dam.saruman.entity.Enemigo;
import dam.saruman.repository.EnemigoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service //DECLARA EL SERVICIO, ES DECIR LA LÓGICA DEL NEGOCIO, LO QUE QUIERO HACER CON LOS DATOS
public class EnemigoService {

    @Autowired // SPRING ME CONECTA AUTOMÁTICAMENTE EL REPOSITORIO CON EL SERVICE, SIN NECESITAR CREARLO MANUALMENTE
    private EnemigoRepository enemigoRepository; // IMPORTO EL REPOSITORIO, EL QUE HABLA CON LA BASE DE DATOS

    // ESTE METODO HACE UNA LISTA CON TODOS LOS ENEMIGOS
    public List<Enemigo> obtenerTodos(){
        List<Enemigo> enemigos = enemigoRepository.findAll(); // USAMOS LA FUNCION QUE YA VIENE EN JPA PRA BUSCARLOS A TODOS
        if(enemigos.isEmpty()){
            System.out.println("Acho que esto esta to triste");
        }else{
            System.out.println("Jefe esto va como una maquina");
            enemigos.forEach(enemigo-> {
                // IMPRIMIMOS DATOS DE CADA ENEMIGO
                System.out.println("ID: "+enemigo.getId() + " Nombre: "+enemigo.getName()+
                        " pais: "+enemigo.getPais()+" afiliación: "+enemigo.getAfiliacion_politica());
            });
        }
        return enemigos; // DEVOLVEMOS LA LISTA CON TODOS
    }

    // ESTE METODO GUARDA UN ENEMIGO NUEVO
    public Enemigo guardar(Enemigo enemigo){
        return enemigoRepository.save(enemigo); // JPA SE ENCARGA DE GUARDARLO EN LA BASE DE DATOS
    }

    // ESTE METODO ELIMINA UN ENEMIGO POR ID
    public boolean eliminar(Long id){
        try {
            if(enemigoRepository.existsById(id)){ // VEMOS SI EXISTE Y
                enemigoRepository.deleteById(id); // LO BORRAMOS
                System.out.println("Enemigo con ID " + id + " eliminado correctamente");
                return true;
            } else {
                System.out.println("No existe enemigo con ID " + id);
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error al eliminar: " + e.getMessage());
            return false;
        }
    }

    // ESTE METODO ACTUALIZA UN ENEMIGO EXISTENTE
    public Enemigo actualizar(Long id, Enemigo enemigoActualizado){
        Optional<Enemigo> enemigoExistente = enemigoRepository.findById(id); // BUSCAMOS SI EXISTE

        if(enemigoExistente.isPresent()){
            Enemigo enemigo = enemigoExistente.get();
            // ACTUALIZAMOS LOS CAMPOS
            enemigo.setName(enemigoActualizado.getName());
            enemigo.setPais(enemigoActualizado.getPais());
            enemigo.setAfiliacion_politica(enemigoActualizado.getAfiliacion_politica());

            System.out.println("Enemigo con ID " + id + " actualizado correctamente");
            return enemigoRepository.save(enemigo); // GUARDAMOS LOS CAMBIOS
        } else {
            System.out.println("No existe enemigo con ID " + id);
            return null;
        }
    }
}