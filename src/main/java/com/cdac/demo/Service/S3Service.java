package com.cdac.demo.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;


import com.amazonaws.*;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
public class S3Service implements FileServiceImpl{

	@Value("${bucketName}")
	private String bucketName;
	
	private final AmazonS3 s3;
	
	
	public S3Service(AmazonS3 s3) {
		this.s3 = s3;
	}
	
	@Override
	public String saveFile(MultipartFile file) {
		
		String originalFileName = file.getOriginalFilename();
		
		try {
			File file1 = convertMultipartFileToFile(file);
			PutObjectResult putObjectResult = s3.putObject(bucketName,originalFileName,file1);
			return putObjectResult.getContentMd5();
		}
		catch(IOException e) {
			throw new RuntimeException(e);
		}
		
		
	}

	@Override
	public byte[] downloadFile(String fileName) {
		
		S3Object object = s3.getObject(bucketName,fileName);
		System.out.println("fileName : "+fileName);
		S3ObjectInputStream objectContent = object.getObjectContent();
		try {
			return IOUtils.toByteArray(objectContent);
		}
		catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String deleteFile(String fileName) {

		s3.deleteObject(bucketName, fileName);
		return "File Deleted";
	}

	@Override
	public List<String> listAllFiles() {

		ListObjectsV2Result listObjectsV2Result = s3.listObjectsV2(bucketName);
		return listObjectsV2Result.getObjectSummaries().stream().map(o->o.getKey()).collect(Collectors.toList());
	}

	
	public File convertMultipartFileToFile(MultipartFile file) throws IOException {
		File conFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(conFile);
		fos.write(file.getBytes());
		fos.close();
		return conFile;
	}
	
}
