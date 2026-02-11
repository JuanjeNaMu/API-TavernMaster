package dam.tavernmaster.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // Aquí se encripta y se define cómo se encripta la contraseña
        // para no meterla en la BBDD en texto plano
        // El "12" es la fuerza del hash
        return new BCryptPasswordEncoder(12);
    }
}
