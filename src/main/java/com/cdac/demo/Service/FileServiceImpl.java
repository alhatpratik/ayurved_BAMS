package com.cdac.demo.Service;

import java.io.FileInputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface FileServiceImpl {

	String saveFile(MultipartFile fFile);
	byte[] downloadFile(String fileName);
	String deleteFile(String fileName);
	List<String> listAllFiles();
	
}
