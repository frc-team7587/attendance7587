package org.memo.frc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class AttendanceApplication 
	extends SpringBootServletInitializer{		// needed for running .war in tomcat

	public static void main(String[] args) {
		SpringApplication.run(AttendanceApplication.class, args);
	}
}
