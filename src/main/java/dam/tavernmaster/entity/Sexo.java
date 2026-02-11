package dam.tavernmaster.entity;

public enum Sexo {
    Hombre,
    Mujer,
    Otro,
    Vacio;  // Representa el string vacío '' en la base de datos

    // Convierte un String al enum Sexo correspondiente
    // Si viene null o vacío, devuelve Vacio
    public static Sexo fromString(String value) {
        if (value == null || value.isEmpty()) return Vacio;
        return Sexo.valueOf(value);
    }
}

// NOTAS MENTALES:
// - Esto es un Enum, no una entidad (no tiene @Entity)
// - Sirve para tener valores fijos de sexo y no escribir "Hombre" a mano cada vez
// - En la BD se guarda como 'Hombre', 'Mujer', 'Otro' o ''
// - Vacio es para cuando el usuario no selecciona nada
// - Uso: Jugador.setSexo(Sexo.Hombre);
// - fromString() lo uso cuando llega un String del frontend y lo tengo que convertir a Enum