package dam.tavernmaster;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerarHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

        // Cambia estas contraseñas por las tuyas
        String[] passwords = {
                "admin123",    // DarkBlade
                "loba123",     // LunaWolf
                "mago123",     // PixelMage
                "corredor123", // SkyRunner
                "admin1234",   // admin
                "admin5678"    // admin2
        };

        System.out.println("=" .repeat(80));
        System.out.println("🔐 GENERA TUS HASHES PARA FLYWAY");
        System.out.println("=" .repeat(80));

        for (String pwd : passwords) {
            String hash = encoder.encode(pwd);
            System.out.println("Contraseña: '" + pwd + "'");
            System.out.println("Hash:       '" + hash + "'");
            System.out.println("-".repeat(80));
        }
    }
}