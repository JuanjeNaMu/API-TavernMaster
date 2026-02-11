package dam.tavernmaster.dto;

public class RegistroJugadorDTO {
    // DTO = Data Transfer Object
    // Sirve para traer los datos del registro desde el frontend (React, Postman, etc.)
    // Solo transporta datos

    private String nombreJug;
    private String password;  // Contraseña en texto plano (SOLO para recibir)
    private String email;
    private String sexo;
    private Boolean esAdmin;

    // Getters y Setters
    // Aquí no hay lógica, solo guardar y devolver datos

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

// NOTA MENTAL:
// Este DTO lo uso en el controlador de Jugador para el registro
// Recibo los datos en texto plano y luego:
// 1. Encripto la contraseña con BCrypt
// 2. Convierto el DTO a una entidad Jugador
// 3. Guardo en la BD
//
// La contraseña en texto plano SOLO existe aquí y en la petición HTTP
// En la BD nunca se guarda sin encriptar