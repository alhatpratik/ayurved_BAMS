package com.cdac.demo.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {

	@Value("${accessKeyId}")
	public String accessKey;
	
	@Value("${secretKey}")
	public String secretKey;
	
	@Value("${region}")
	public String region;
	

	@Bean
	public AmazonS3 s3() {
		
		AWSCredentials awsCred = new BasicAWSCredentials(accessKey, secretKey);
		return AmazonS3ClientBuilder.standard().withRegion(region).withCredentials(new AWSStaticCredentialsProvider(awsCred)).build();
		
		
	} 
}
