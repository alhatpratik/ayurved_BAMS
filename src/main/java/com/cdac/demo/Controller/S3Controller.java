package com.cdac.demo.Controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.cdac.demo.Service.S3Service;

@RestController
@RequestMapping("/ayurved")
public class S3Controller {


	private static final HttpStatusCode HTTP_OK = null;
	@Autowired
	private S3Service s3Service;
	
	@PostMapping("/upload")
	public String upload(@RequestParam("file") MultipartFile file){
		System.out.println("inside upload");
		return s3Service.saveFile(file);
	}
	
	@GetMapping("download/{fileName}")
	//public ResponseEntity<FileInputStream> download(@PathVariable("fileName") String fileName) {
		
	public ResponseEntity<String> download(@PathVariable("fileName") String fileName) {
		System.out.println("inside download "+fileName);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type",MediaType.ALL_VALUE);
		headers.add("Content-Disposition", "attachment; fileName="+fileName);
		byte[] bytes = s3Service.downloadFile(fileName);
		
		System.out.println("Successfully done ... "+ bytes);
		//return ResponseEntity.status(HTTP_OK).headers(headers).body(bytes);
		
		ResponseEntity.status(200).headers(headers).body(bytes);
		//return ResponseEntity.status(200).headers(headers).body(bytes);

		//-------------------------------------
		
		try {
            // Convert byte array to InputStream
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            FileInputStream f_in = new FileInputStream(createTempFile(byteArrayInputStream));

            // Your logic with the FileInputStream goes here

            XSSFWorkbook workbook = null;
            
			try {

				workbook = new XSSFWorkbook(f_in);

			} catch (IOException e) {

				System.out.println("problem in workbook");
			}

			XSSFSheet sheet = workbook.getSheetAt(0);

			//----------------------------------------------------------------------ITERATOR LOGIC-------

			Iterator<Row> rowIt = sheet.iterator();
			while(rowIt.hasNext())
			{
				Row row = rowIt.next();
				Iterator<Cell> cellIterator =row.cellIterator();

				String rowSplit = "";
				while(cellIterator.hasNext())
				{
					Cell cell = cellIterator.next();
					rowSplit += cell.toString()+";";       // seperate each value of sheet column by ; semicolon 
					System.out.println("inside +++ "+ rowSplit);
				}            
            
			}
            //--------------------------------------------------------------------------
         
            return ResponseEntity.ok("Successfully converted and processed the byte array.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error converting byte array to InputStream.");
        }
				
	}
	
	
	private File createTempFile(ByteArrayInputStream byteArrayInputStream) throws IOException {
        // You can customize the file name and location as needed
        File tempFile = File.createTempFile("temp", ".tmp");

        // Write the InputStream data to the temporary file
        org.apache.commons.io.FileUtils.copyInputStreamToFile(byteArrayInputStream, tempFile);

        // Optionally, set the temporary file to be deleted on exit
        tempFile.deleteOnExit();

        return tempFile;
    }
	
	@DeleteMapping("{}fileName")
	public String deleteFile(@PathVariable("fileName") String fileName) {
		System.out.println("inside deleteFiles");
		return s3Service.deleteFile(fileName);
	}
	
	@GetMapping("getAllFiles")
	public List<String> getAllFiles() {
		System.out.println("inside getAllFiles");
		return s3Service.listAllFiles();
	}

}
