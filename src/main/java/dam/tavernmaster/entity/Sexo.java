package dam.tavernmaster.entity;

public enum Sexo {
    Hombre,
    Mujer,
    Otro,
    Vacio;  // Representa el string vacío '' en la base de datos

    public static Sexo fromString(String value) {
        if (value == null || value.isEmpty()) return Vacio;
        return Sexo.valueOf(value);
    }
}