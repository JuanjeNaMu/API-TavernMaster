package dam.tavernmaster.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FichaConAtaquesDTO {

    @JsonProperty("id_ficha")
    private Integer idFicha;

    @JsonProperty("id_per")
    private Integer idPer;

    @JsonProperty("clase")
    private String clase;

    @JsonProperty("fuerza")
    private Integer fuerza;

    @JsonProperty("destreza")
    private Integer destreza;

    @JsonProperty("constitucion")
    private Integer constitucion;

    @JsonProperty("inteligencia")
    private Integer inteligencia;

    @JsonProperty("sabiduria")
    private Integer sabiduria;

    @JsonProperty("carisma")
    private Integer carisma;

    @JsonProperty("ataques")
    private List<AtaqueDTO> ataques;

    public Integer getIdFicha() {
        return idFicha;
    }

    public void setIdFicha(Integer idFicha) {
        this.idFicha = idFicha;
    }

    public Integer getIdPer() {
        return idPer;
    }

    public void setIdPer(Integer idPer) {
        this.idPer = idPer;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public Integer getFuerza() {
        return fuerza;
    }

    public void setFuerza(Integer fuerza) {
        this.fuerza = fuerza;
    }

    public Integer getDestreza() {
        return destreza;
    }

    public void setDestreza(Integer destreza) {
        this.destreza = destreza;
    }

    public Integer getConstitucion() {
        return constitucion;
    }

    public void setConstitucion(Integer constitucion) {
        this.constitucion = constitucion;
    }

    public Integer getInteligencia() {
        return inteligencia;
    }

    public void setInteligencia(Integer inteligencia) {
        this.inteligencia = inteligencia;
    }

    public Integer getSabiduria() {
        return sabiduria;
    }

    public void setSabiduria(Integer sabiduria) {
        this.sabiduria = sabiduria;
    }

    public Integer getCarisma() {
        return carisma;
    }

    public void setCarisma(Integer carisma) {
        this.carisma = carisma;
    }

    public List<AtaqueDTO> getAtaques() {
        return ataques;
    }

    public void setAtaques(List<AtaqueDTO> ataques) {
        this.ataques = ataques;
    }
}
