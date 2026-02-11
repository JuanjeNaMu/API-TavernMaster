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
    @JsonProperty("id_per")           // 👈 AÑADIDO: Para que el JSON use el mismo nombre que la BD
    private Integer idPer;
    // Clave primaria, se autoincrementa

    @Column(name = "jugador_padre")
    @JsonProperty("jugador_padre")    // 👈 AÑADIDO: Para que el JSON use el mismo nombre que la BD
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
    @JsonProperty("nombre_per")       // 👈 AÑADIDO: Para que el JSON use el mismo nombre que la BD
    private String nombrePer;
    // Nombre del personaje

    @Column(name = "nivel")
    @JsonProperty("nivel")           // 👈 AÑADIDO: Para que el JSON use el mismo nombre que la BD
    private Integer nivel;
    // Nivel del personaje

    @Column(name = "imagen_base64", columnDefinition = "TEXT")
    @JsonProperty("imagen_base64")   // 👈 AÑADIDO: Para que el JSON use el mismo nombre que la BD
    private String imagenBase64;
    // Imagen del personaje en base64 (texto largo)
    // Se guarda como TEXT en la BD

    // Getter virtual para obtener SOLO el ID de la campaña
    @JsonProperty("id_cam")          // 👈 ESTE YA ESTABA BIEN
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
// - id_per se genera automático, NUNCA asignarlo manualmente
// - jugador_padre es OBLIGATORIO (la columna en BD es NOT NULL)
// - campana puede ser null (personaje sin campaña asignada)
// - JsonBackReference evita el bucle infinito con Campana
// - Los @JsonProperty en los CAMPOS hacen que:
//     * Al recibir JSON, mapee "id_per" del JSON a idPer en Java
//     * Al devolver JSON, serialice idPer como "id_per"
// - El método getIdCam() sigue siendo necesario para mostrar SOLO el ID de la campaña
// - AHORA EL JSON COINCIDE EXACTAMENTE CON LOS NOMBRES DE LA BD ✅