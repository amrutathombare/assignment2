package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Myvote2Application {

    public static void main(String[] args) {
        SpringApplication.run(Myvote2Application.class, args);
    }
}
