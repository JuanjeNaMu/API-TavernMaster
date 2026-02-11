package dam.tavernmaster.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "JUGADOR")
public class Jugador {

    // Entidad JPA que representa a un usuario del sistema
    // Puede ser un jugador normal o un administrador

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_jugador")
    private Integer id;
    // Clave primaria, se autoincrementa

    @Column(name = "EsAdmin", nullable = false)
    private Boolean esAdmin = false;
    // Por defecto NO es admin
    // true = administrador, false = jugador normal

    @Column(name = "nombre_jug", nullable = false, length = 100)
    private String nombreJug;
    // Nombre de usuario, único en la aplicación

    @Column(name = "password", nullable = false, length = 100)
    private String password;
    // Contraseña ENCRIPTADA con BCrypt
    // NUNCA se guarda en texto plano

    @Column(name = "email", nullable = false, length = 100)
    private String email;
    // Email del jugador

    @Column(name = "fecha_nac")
    private LocalDate fechaNac;
    // Fecha de nacimiento, puede ser null

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo", columnDefinition = "enum('Hombre','Mujer','Otro','')")
    private Sexo sexo;
    // Usamos Enum para sexo (Hombre, Mujer, Otro, vacío)
    // EnumType.STRING guarda el nombre del enum en la BD

    @OneToMany(mappedBy = "jugadorPadre")
    private List<Personaje> personajes;
    // Relación 1 a N: Un jugador tiene muchos personajes
    // mappedBy = "jugadorPadre" -> Personaje es el dueño de la relación

    // Constructor vacío requerido por JPA
    public Jugador() {}

    // Getters y Setters
    public Integer getId() { return id; }
    // No hay setId porque el ID es autogenerado, no se debe modificar manualmente

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

// NOTAS MENTALES:
// - El ID es autogenerado, NUNCA asignarlo manualmente
// - La contraseña SIEMPRE encriptada antes de guardar
// - esAdmin por defecto false, solo se cambia explícitamente
// - Sexo es un Enum, en BD se guarda como 'Hombre', 'Mujer', etc.
// - Para traer los personajes de un jugador necesito hacer fetch explícito