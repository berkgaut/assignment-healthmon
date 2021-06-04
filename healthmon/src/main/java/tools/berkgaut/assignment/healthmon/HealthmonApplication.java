package tools.berkgaut.assignment.healthmon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HealthmonApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthmonApplication.class, args);
	}

}
