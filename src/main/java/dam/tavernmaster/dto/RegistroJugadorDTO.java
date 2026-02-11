package dam.tavernmaster.dto;

public class RegistroJugadorDTO {
    private String nombreJug;
    private String password;  // Contraseña en texto plano (SOLO para recibir)
    private String email;
    private String sexo;
    private Boolean esAdmin;

    // Getters y Setters
    public String getNombreJug() { return nombreJug; }
    public void setNombreJug(String nombreJug) { this.nombreJug = nombreJug; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public Boolean getEsAdmin() { return esAdmin; }
    public void setEsAdmin(Boolean esAdmin) { this.esAdmin = esAdmin; }
}