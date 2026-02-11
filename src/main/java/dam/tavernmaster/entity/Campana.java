package dam.tavernmaster.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "CAMPANA")
public class Campana {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cam")
    private Integer idCam;

    @Column(name = "master", nullable = false, length = 100)
    private String master;

    @Column(name = "titulo", length = 100)
    private String titulo;

    @Column(name = "proxima_sesion")
    private LocalDate proximaSesion;

    @Column(name = "encuentros")
    private Integer encuentros;

    @OneToMany(mappedBy = "campana")
    @JsonManagedReference  // ✅ Este es el padre
    private List<Personaje> personajes;

    // Constructor vacío
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