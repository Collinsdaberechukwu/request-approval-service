package com.collins.request_approval_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "AuditAwareImpl")
public class RequestApprovalServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RequestApprovalServiceApplication.class, args);
	}

}
