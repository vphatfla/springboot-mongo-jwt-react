package com.example.springjwtmongoreact;

import com.example.springjwtmongoreact.models.ERole;
import com.example.springjwtmongoreact.models.Role;
import com.example.springjwtmongoreact.models.User;
import com.example.springjwtmongoreact.repository.RoleRepository;
import com.example.springjwtmongoreact.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class SpringJwtMongoReactApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringJwtMongoReactApplication.class, args);
	}

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;
	public void createUser() {
		userRepository.save(new User("harrypotter", "chaser@gmail.com", "123456789"));
	}

	public void createRole() {
		roleRepository.save(new Role(ERole.ROLE_USER));
		roleRepository.save(new Role(ERole.ROLE_MODERATOR));
		roleRepository.save(new Role(ERole.ROLE_ADMIN));
	}
	@Override
	public void run(String... args) throws Exception {

		System.out.println("Start running");
		System.out.println("Start deleting");
		//userRepository.deleteAll();
		//roleRepository.deleteAll();
		System.out.println("Start creating");
		//createUser();
		//createRole();
		System.out.println("Done running");
	}
}
