package dam.tavernmaster.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "PERSONAJE")
public class Personaje {

    // Entidad JPA que representa a un personaje de rol
    // Cada personaje pertenece a un jugador y puede estar en una campaña

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_per")
    private Integer idPer;
    // Clave primaria, se autoincrementa

    @Column(name = "jugador_padre")
    private String jugadorPadre;
    // Nombre del jugador dueño del personaje (relación con Jugador por nombre)

    @ManyToOne
    @JoinColumn(name = "id_cam")
    @JsonBackReference
    private Campana campana;
    // Relación N a 1: Muchos personajes pueden estar en una misma campaña
    // JoinColumn define la FK en la tabla PERSONAJE
    // JsonBackReference evita el bucle infinito con Campana

    @Column(name = "nombre_per")
    private String nombrePer;
    // Nombre del personaje

    @Column(name = "nivel")
    private Integer nivel;
    // Nivel del personaje

    @Column(name = "imagen_base64", columnDefinition = "TEXT")
    private String imagenBase64;
    // Imagen del personaje en base64 (texto largo)
    // Se guarda como TEXT en la BD

    // NUEVO: Campo para obtener SOLO el ID de la campaña
    @JsonProperty("id_cam")
    public Integer getIdCam() {
        // Esto NO es un campo de la BD
        // Es un getter virtual para que el JSON devuelva solo el ID de la campaña
        // y no el objeto Campana entero
        return campana != null ? campana.getIdCam() : null;
    }

    // Constructor vacío
    public Personaje() {}

    // Getters y Setters
    public Integer getIdPer() { return idPer; }
    public void setIdPer(Integer idPer) { this.idPer = idPer; }

    public String getJugadorPadre() { return jugadorPadre; }
    public void setJugadorPadre(String jugadorPadre) { this.jugadorPadre = jugadorPadre; }

    public Campana getCampana() { return campana; }
    public void setCampana(Campana campana) { this.campana = campana; }

    public String getNombrePer() { return nombrePer; }
    public void setNombrePer(String nombrePer) { this.nombrePer = nombrePer; }

    public Integer getNivel() { return nivel; }
    public void setNivel(Integer nivel) { this.nivel = nivel; }

    public String getImagenBase64() { return imagenBase64; }
    public void setImagenBase64(String imagenBase64) { this.imagenBase64 = imagenBase64; }
}

// NOTAS MENTALES:
// - idPer se genera automático
// - jugadorPadre es un String con el nombre del jugador, no la entidad Jugador completa
// - campana puede ser null (personaje sin campaña asignada)
// - JsonBackReference evita que al devolver Campana se intente devolver Personaje otra vez
// - El método getIdCam() es solo para el JSON, no es columna en BD
// - Cuando devuelvo un personaje, el JSON incluye "id_cam": 5 en vez de todo el objeto campana