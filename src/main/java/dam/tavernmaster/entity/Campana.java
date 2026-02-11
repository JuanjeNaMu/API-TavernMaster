package dam.tavernmaster.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "CAMPANA")
public class Campana {

    // Esta clase es una entidad JPA -> representa la tabla CAMPANA en la BD
    // Cada campaña es una partida de rol dirigida por un master

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cam")
    private Integer idCam;
    // Clave primaria, se autoincrementa sola en la BD

    @Column(name = "master", nullable = false, length = 100)
    private String master;
    // Nombre del dungeon master, no puede ser nulo

    @Column(name = "titulo", length = 100)
    private String titulo;
    // Título de la campaña

    @Column(name = "proxima_sesion")
    private LocalDate proximaSesion;
    // Fecha de la próxima sesión, si es null es que no hay fecha

    @Column(name = "encuentros")
    private Integer encuentros;
    // Número de encuentros/sesiones que llevan

    @OneToMany(mappedBy = "campana")
    @JsonManagedReference  // Este es el padre
    private List<Personaje> personajes;
    // Relación 1 a N: Una campaña tiene muchos personajes
    // mappedBy = "campana" -> la entidad Personaje es la dueña de la relación
    // JsonManagedReference evita el bucle infinito al serializar JSON

    // Constructor vacío (obligatorio para JPA)
    public Campana() {}

    // Getters y Setters
    public Integer getIdCam() { return idCam; }
    public void setIdCam(Integer idCam) { this.idCam = idCam; }

    public String getMaster() { return master; }
    public void setMaster(String master) { this.master = master; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public LocalDate getProximaSesion() { return proximaSesion; }
    public void setProximaSesion(LocalDate proximaSesion) { this.proximaSesion = proximaSesion; }

    public Integer getEncuentros() { return encuentros; }
    public void setEncuentros(Integer encuentros) { this.encuentros = encuentros; }

    public List<Personaje> getPersonajes() { return personajes; }
    public void setPersonajes(List<Personaje> personajes) { this.personajes = personajes; }
}

// NOTAS MENTALES:
// - El ID se genera automático, NO lo asigno yo
// - Si pido una campaña con get, los personajes NO vienen por defecto (Lazy)
// - Para traer personajes tengo que hacer la query específica con JOIN FETCH
// - JsonManagedReference hace que al devolver JSON se muestren los personajes
//   pero sin que Personaje intente devolver a Campana y se forme un bucle