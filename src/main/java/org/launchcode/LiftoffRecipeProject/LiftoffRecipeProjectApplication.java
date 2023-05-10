package org.launchcode.LiftoffRecipeProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
public class LiftoffRecipeProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiftoffRecipeProjectApplication.class, args);
	}

}
