package com.cdac.logic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AWSFIle {

	
	public String checkingForAWS() {
		
		
		
        
        
        
		
		try {

			FileInputStream f_in = null;				// logic to fetch list of all patients from Excel File
			try {

				f_in = new FileInputStream("D:\\abc.xlsx");

			} catch (FileNotFoundException e) {

				return "File Not Found";
			}

			XSSFWorkbook workbook = null;

			try {

				workbook = new XSSFWorkbook(f_in);

			} catch (IOException e) {

				return "problem in workbook";
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
				}											// and concat each row of sheet with ; semicolon
				System.out.println(rowSplit);				//	by which further we can split that in each column String

			}

			System.out.println();

		} 
		catch(NullPointerException e)
		{
			return "Cannot perform Action";
		}
		
		return null;
	
	}
	
}
