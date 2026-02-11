package dam.tavernmaster.utility;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerarHash {
    public static void main(String[] args) {
        // Clase UTILIDAD, no forma parte de la aplicación
        // Sirve para generar hashes de contraseñas fuera de la app
        // La ejecuto manualmente cuando necesito crear usuarios en Flyway

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        // Misma fuerza de hash que en SecurityConfig (12)

        // Cambia estas contraseñas por las tuyas
        String[] passwords = {
                "admin123",    // DarkBlade
                "loba123",     // LunaWolf
                "mago123",     // PixelMage
                "corredor123", // SkyRunner
                "admin1234",   // admin
                "admin5678"    // admin2
        };

        System.out.println("=".repeat(80));
        System.out.println("GENERA TUS HASHES PARA FLYWAY");
        System.out.println("=".repeat(80));

        for (String pwd : passwords) {
            String hash = encoder.encode(pwd);
            System.out.println("Contraseña: '" + pwd + "'");
            System.out.println("Hash:       '" + hash + "'");
            System.out.println("-".repeat(80));
        }
    }
}

// NOTAS MENTALES:
// - NO es un test, es una clase main que ejecuto cuando quiero
// - Sirve para generar contraseñas encriptadas para meterlas en los scripts SQL de Flyway
// - Cada vez que ejecuto sale un hash DIFERENTE aunque la contraseña sea la misma (BCrypt genera salt aleatorio)
// - Cualquier hash que genere es válido, no importa que sea diferente cada vez
// - Copio el hash que sale y lo pego en el INSERT INTO de mi archivo SQL