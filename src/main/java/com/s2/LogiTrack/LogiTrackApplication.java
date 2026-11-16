package com.s2.LogiTrack;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class LogiTrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogiTrackApplication.class,args);
		System.out.println("Hola");
		System.out.println("TODO VA BIEN BRO");
	}

}
