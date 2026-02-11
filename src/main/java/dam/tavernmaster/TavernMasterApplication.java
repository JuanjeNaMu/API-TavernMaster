package dam.tavernmaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TavernMasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(TavernMasterApplication.class, args);
        System.out.println("||    TAVERN MASTER API INICIADA     ||");
    }
}