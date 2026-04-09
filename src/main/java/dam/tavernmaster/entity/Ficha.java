package dam.tavernmaster.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "FICHA")
public class Ficha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ficha")
    @com.fasterxml.jackson.annotation.JsonProperty("id_ficha")
    private Integer idFicha;

    @Column(name = "clase")
    @com.fasterxml.jackson.annotation.JsonProperty("clase")
    private String clase;

    @Column(name = "fuerza")
    @com.fasterxml.jackson.annotation.JsonProperty("fuerza")
    private Integer fuerza;

    @Column(name = "destreza")
    @com.fasterxml.jackson.annotation.JsonProperty("destreza")
    private Integer destreza;

    @Column(name = "constitucion")
    @com.fasterxml.jackson.annotation.JsonProperty("constitucion")
    private Integer constitucion;

    @Column(name = "inteligencia")
    @com.fasterxml.jackson.annotation.JsonProperty("inteligencia")
    private Integer inteligencia;

    @Column(name = "sabiduria")
    @com.fasterxml.jackson.annotation.JsonProperty("sabiduria")
    private Integer sabiduria;

    @Column(name = "carisma")
    @com.fasterxml.jackson.annotation.JsonProperty("carisma")
    private Integer carisma;

    // You might also want to add other common attributes like:
    // private String nombre;
    // private Integer nivel;
    // private Integer vida;
    // private Integer experiencia;
    // etc.

    // Constructors
    public Ficha() {
    }

    public Ficha(String clase, Integer fuerza, Integer destreza, Integer constitucion,
                 Integer inteligencia, Integer sabiduria, Integer carisma) {
        this.clase = clase;
        this.fuerza = fuerza;
        this.destreza = destreza;
        this.constitucion = constitucion;
        this.inteligencia = inteligencia;
        this.sabiduria = sabiduria;
        this.carisma = carisma;
    }

    // Getters and Setters
    public Integer getIdFicha() {
        return idFicha;
    }

    public void setIdFicha(Integer idFicha) {
        this.idFicha = idFicha;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ficha ficha = (Ficha) o;
        return Objects.equals(idFicha, ficha.idFicha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFicha);
    }

    @Override
    public String toString() {
        return "Ficha{" +
                "idFicha=" + idFicha +
                ", clase='" + clase + '\'' +
                ", fuerza=" + fuerza +
                ", destreza=" + destreza +
                ", constitucion=" + constitucion +
                ", inteligencia=" + inteligencia +
                ", sabiduria=" + sabiduria +
                ", carisma=" + carisma +
                '}';
    }
}