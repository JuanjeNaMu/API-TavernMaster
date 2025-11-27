package dam.saruman.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "enemigoestado")
public class Enemigo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "afiliacion_politica") //ESTO PODRÍA SERVIR PARA MANTENER LA REFERENCIA EN LA TABLA PUDIENDO CAMBIAR EL NOMBRE EN LOS MÉTODOS
    private String afiliacion_politica;  //    private String a; //seguiría sirviendo ya que haría referencia a la columna afiliacion_politica

    private String pais;

    public Enemigo() {
    }

// Constructor
    public Enemigo(String name, String afiliacion_politica, String pais) {
        this.name = name;
        this.afiliacion_politica = afiliacion_politica;
        this.pais = pais;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    //public void setId(Long id) {this.id = id;} NO ES NECESARIO PORQUE EL ID SE SETEA DE MAENRA AUTOMÁTICA

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAfiliacion_politica() {
        return afiliacion_politica;
    }

    public void setAfiliacion_politica(String afiliacion_politica) {
        this.afiliacion_politica = afiliacion_politica;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}