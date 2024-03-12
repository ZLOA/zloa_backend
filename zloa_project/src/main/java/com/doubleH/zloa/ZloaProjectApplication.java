package com.doubleH.zloa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ZloaProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZloaProjectApplication.class, args);
	}

}
