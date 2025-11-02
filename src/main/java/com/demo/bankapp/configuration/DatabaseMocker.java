package com.demo.bankapp.configuration;

import com.demo.bankapp.controller.UserController;
import com.demo.bankapp.repository.UserRepository;
import com.demo.bankapp.request.CreateUserRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
public class DatabaseMocker {

	@Value("${app.mock-data.enabled:false}")
	private boolean mockDataEnabled;

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	CommandLineRunner initDatabase(UserRepository repository, UserController userController) {
		return args -> {
			if (!mockDataEnabled) {
				System.out.println("Mock data creation is disabled. Skipping database initialization.");
				return;
			}

			System.out.println("Mock data creation is enabled. Initializing database...");

			CreateUserRequest cnuRequest = new CreateUserRequest();
			cnuRequest.setUsername("marco_polo77");
			cnuRequest.setPassword("secure@2025");
			cnuRequest.setTcno("15049382761");

			CreateUserRequest cnuRequest2 = new CreateUserRequest();
			cnuRequest2.setUsername("luna_sky");
			cnuRequest2.setPassword("!starGazer3");
			cnuRequest2.setTcno("40875632912");

			CreateUserRequest cnuRequest3 = new CreateUserRequest();
			cnuRequest3.setUsername("sam_williams");
			cnuRequest3.setPassword("Wiz_ard_9");
			cnuRequest3.setTcno("29381745063");

			// Initialize for the first time
			userController.createUser(cnuRequest);
			userController.createUser(cnuRequest2);
			userController.createUser(cnuRequest3);
		};
	}
}