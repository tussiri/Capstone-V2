package org.launchcode.LiftoffRecipeProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiftoffRecipeProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiftoffRecipeProjectApplication.class, args);
	}

//	@Bean
//	public WebMvcConfigurer corsConfigurer(){
//		return new WebMvcConfigurer(){
//			@Override
//			public void addCorsMappings(CorsRegistry registry){
//				registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
//			}
//		};
//	}

}