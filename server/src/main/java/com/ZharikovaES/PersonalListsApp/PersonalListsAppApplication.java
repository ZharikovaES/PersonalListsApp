package com.ZharikovaES.PersonalListsApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.ZharikovaES.PersonalListsApp.repos")
@EntityScan("com.ZharikovaES.PersonalListsApp.models")
@ComponentScan("com.ZharikovaES.PersonalListsApp.*")
public class PersonalListsAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(PersonalListsAppApplication.class, args);
	}

}
