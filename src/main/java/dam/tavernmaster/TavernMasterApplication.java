package dam.tavernmaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TavernMasterApplication {

    public static void main(String[] args) {
        // PUNTO DE ENTRADA DE TODA LA APLICACIÓN
        // Esto es lo que ejecuto para arrancar el backend

        SpringApplication.run(TavernMasterApplication.class, args);
        // Arranca Spring Boot, levanta el servidor embebido (Tomcat)
        // Escanea todos los paquetes en busca de @Controller, @Service, @Repository, @Entity
        // Configura la conexión a la BD, JPA, etc.

        System.out.println("||    TAVERN MASTER API INICIADA     ||");
        // Chulada visual para saber que ha arrancado bien
    }
}

// NOTAS MENTALES:
// - @SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan
// - Sin esta clase, no arranca nada
// - Solo debe haber UNA clase con @SpringBootApplication y main
// - El println es solo mío, no es necesario pero queda bonito