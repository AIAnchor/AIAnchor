package com.boradcast.aianchor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class AianchorApplication {

	public static void main(String[] args) {
		try {
			SpringApplication.run(AianchorApplication.class, args);
		} catch (Exception e) {
		    System.err.println("\n\n !!!예외 발생: " + e.getMessage());
		    e.printStackTrace();
		}
	}
}
