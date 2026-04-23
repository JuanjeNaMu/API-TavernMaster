package dam.tavernmaster.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "ATAQUE")
public class Ataque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ataque")
    @JsonProperty("id_ataque")
    private Integer idAtaque;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_per", nullable = false)
    @JsonIgnore
    private Personaje personaje;

    @Column(name = "nombre", nullable = false, length = 120)
    @JsonProperty("nombre")
    private String nombre;

    @Column(name = "caracteristica", nullable = false, length = 40)
    @JsonProperty("caracteristica")
    private String caracteristica;

    @Column(name = "es_competente", nullable = false)
    @JsonProperty("es_competente")
    private Boolean esCompetente = false;

    @JsonProperty("id_per")
    public Integer getIdPer() {
        return personaje != null ? personaje.getIdPer() : null;
    }

    public Integer getIdAtaque() {
        return idAtaque;
    }

    public void setIdAtaque(Integer idAtaque) {
        this.idAtaque = idAtaque;
    }

    public Personaje getPersonaje() {
        return personaje;
    }

    public void setPersonaje(Personaje personaje) {
        this.personaje = personaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCaracteristica() {
        return caracteristica;
    }

    public void setCaracteristica(String caracteristica) {
        this.caracteristica = caracteristica;
    }

    public Boolean getEsCompetente() {
        return esCompetente;
    }

    public void setEsCompetente(Boolean esCompetente) {
        this.esCompetente = esCompetente;
    }
}
