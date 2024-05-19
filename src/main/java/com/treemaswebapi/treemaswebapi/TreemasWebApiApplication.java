  package com.treemaswebapi.treemaswebapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableScheduling
@EntityScan(basePackages = "com.treemaswebapi.treemaswebapi.entity")
public class TreemasWebApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TreemasWebApiApplication.class, args);
	}

}
