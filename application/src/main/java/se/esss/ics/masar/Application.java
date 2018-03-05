package se.esss.ics.masar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "se.esss.ics.masar")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
