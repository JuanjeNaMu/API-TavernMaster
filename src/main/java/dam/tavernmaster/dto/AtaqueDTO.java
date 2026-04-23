package dam.tavernmaster.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AtaqueDTO {

    @JsonProperty("id_ataque")
    private Integer idAtaque;

    @JsonProperty("id_per")
    private Integer idPer;

    @JsonProperty("nombre")
    private String nombre;

    @JsonProperty("caracteristica")
    private String caracteristica;

    @JsonProperty("es_competente")
    private Boolean esCompetente;

    public Integer getIdAtaque() {
        return idAtaque;
    }

    public void setIdAtaque(Integer idAtaque) {
        this.idAtaque = idAtaque;
    }

    public Integer getIdPer() {
        return idPer;
    }

    public void setIdPer(Integer idPer) {
        this.idPer = idPer;
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
