package dam.tavernmaster.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "JUGADOR")
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_jugador")
    private Integer id;

    @Column(name = "EsAdmin", nullable = false)
    private Boolean esAdmin = false;

    @Column(name = "nombre_jug", nullable = false, length = 100)
    private String nombreJug;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "fecha_nac")
    private LocalDate fechaNac;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo", columnDefinition = "enum('Hombre','Mujer','Otro','')")
    private Sexo sexo;

    @OneToMany(mappedBy = "jugadorPadre")
    private List<Personaje> personajes;

    // Constructor vacío requerido por JPA
    public Jugador() {}

    // Getters y Setters
    public Integer getId() { return id; }

    public Boolean getEsAdmin() { return esAdmin; }
    public void setEsAdmin(Boolean esAdmin) { this.esAdmin = esAdmin; }

    public String getNombreJug() { return nombreJug; }
    public void setNombreJug(String nombreJug) { this.nombreJug = nombreJug; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getFechaNac() { return fechaNac; }
    public void setFechaNac(LocalDate fechaNac) { this.fechaNac = fechaNac; }

    public Sexo getSexo() { return sexo; }
    public void setSexo(Sexo sexo) { this.sexo = sexo; }

    public List<Personaje> getPersonajes() { return personajes; }
    public void setPersonajes(List<Personaje> personajes) { this.personajes = personajes; }
}