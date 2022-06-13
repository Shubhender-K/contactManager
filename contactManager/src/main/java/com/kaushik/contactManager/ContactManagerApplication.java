package com.kaushik.contactManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ContactManagerApplication {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass()); 
	public static void main(String[] args) {
		SpringApplication.run(ContactManagerApplication.class, args);
	}

}
         