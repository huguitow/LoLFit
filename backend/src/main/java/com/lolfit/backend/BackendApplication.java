package com.lolfit.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.lolfit.backend.api.RiotService;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner testRiotConnection(RiotService riotService) {
		return args -> {
			try {
				String version = riotService.getLatestVersion();
				System.out.println("Version Riot: " + version);
			} catch (Exception e) {
				System.err.println("Erreur lors de la connexion à Riot: " + e.getMessage());
			}
		};
	}
	/*
	 * @Bean
	 * public CommandLineRunner runSync(RiotService riotService) {
	 * return args -> {
	 * try {
	 * riotService.syncChampions();
	 * } catch (Exception e) {
	 * e.printStackTrace();
	 * }
	 * };
	 * }
	 */}