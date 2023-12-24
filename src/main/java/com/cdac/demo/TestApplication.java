package com.cdac.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cdac.logic.AWSFIle;

@SpringBootApplication
public class TestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
		AWSFIle aws = new AWSFIle();
		aws.checkingForAWS();
	}

}
