package com.aequilibrium.Transformers;

import com.aequilibrium.Transformers.dynamo.TransformersTable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point of application.
 */
@SpringBootApplication
public class TransformersApplication {

	public static void main(String[] args) {
		// initialize the database
		try {
			new TransformersTable().initialize();
		} catch (Exception e) {
			System.out.println("Failed to initialize database table: ".concat(e.getMessage()));
		}
		// start the spring server
		SpringApplication.run(TransformersApplication.class, args);
	}
}
