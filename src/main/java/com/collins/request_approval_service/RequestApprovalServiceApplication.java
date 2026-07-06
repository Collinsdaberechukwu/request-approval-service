package com.collins.request_approval_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "AuditAwareImpl")
@OpenAPIDefinition(
		info = @Info(
				title = "Request Approval Service API",
				description = "REST API for managing employee requests through a structured approval workflow, " +
						"including request creation, retrieval, updates, and approval lifecycle management.",
				version = "v1",
				contact = @Contact(
						name = "Okafor Collins",
						email = "collinsdaberechi20@gmail.com"
				)
		)
)
public class RequestApprovalServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RequestApprovalServiceApplication.class, args);
	}

}
