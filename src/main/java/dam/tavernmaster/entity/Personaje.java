package dam.tavernmaster.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "PERSONAJE")
public class Personaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_per")
    private Integer idPer;

    @Column(name = "jugador_padre")
    private String jugadorPadre;

    @ManyToOne
    @JoinColumn(name = "id_cam")
    @JsonBackReference
    private Campana campana;

    @Column(name = "nombre_per")
    private String nombrePer;

    @Column(name = "nivel")
    private Integer nivel;

    @Column(name = "imagen_base64", columnDefinition = "TEXT")
    private String imagenBase64;

    // ✅ NUEVO: Campo para obtener SOLO el ID de la campaña
    @JsonProperty("id_cam")
    public Integer getIdCam() {
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