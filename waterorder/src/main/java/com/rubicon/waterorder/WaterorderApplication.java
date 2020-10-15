package com.rubicon.waterorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WaterorderApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaterorderApplication.class, args);
	}

}
