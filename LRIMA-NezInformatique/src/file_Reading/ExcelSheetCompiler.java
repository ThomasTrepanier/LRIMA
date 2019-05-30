package file_Reading;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

public class ExcelSheetCompiler {
	
	static ArrayList<String> indexes;
	
	public static void compileDataSheet(String fileName, String sheetName, int startSheetNb, int endSheetNb) throws FileNotFoundException, IOException, EncryptedDocumentException, InvalidFormatException {
		try (InputStream inp = new FileInputStream(fileName)) {
			Workbook workbook = WorkbookFactory.create(inp);
			
			compileDataSheet(workbook, sheetName, startSheetNb, endSheetNb);
			
			try {
	            FileOutputStream outputStream = new FileOutputStream(fileName);
	            workbook.write(outputStream);
	            workbook.close();
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
	}
	
	public static void verifyIndexRow(Sheet sheet) {
		ArrayList<String> indexes = DataFill.initIndexes();
		Row row = sheet.getRow(0);
		if(row == null)
			row = sheet.createRow(0);
		
		for(int i = 0; i < indexes.size(); i++) {
			Cell c = row.getCell(i);
			String index = indexes.get(i);
			
			if(c == null)
				c = row.createCell(i);
			
			if(!c.getStringCellValue().equals(index)) {
				c.setCellValue(index);
			}
		}
	}
	public static void compileDataSheet(Workbook wb, String sheetName, int startSheetNb, int endSheetNb) {
		ArrayList<XSSFCell[]> cellList = new ArrayList<XSSFCell[]>();
		Sheet newSheet = null;
		int lastRow = 0;
		if(wb.getSheet(sheetName) != null) {
			cellList = getAllCells(wb.getSheet(sheetName));
			newSheet = wb.getSheet(sheetName);
			lastRow = cellList.size();
		} else {
			newSheet = wb.createSheet(sheetName);
		}
		verifyIndexRow(newSheet);
		
		Iterator<Sheet> it = wb.iterator();
		Sheet currentSheet = null;
		
		int i = 0;
		while(it.hasNext() && i < startSheetNb) { //Get rid of all sheets we dont want to compile at beginning
			it.next();
			i++;
		}
		
		while(it.hasNext() && i <= endSheetNb) {
			i++;
			currentSheet = it.next();
			if(currentSheet != null && !currentSheet.getSheetName().contains(sheetName)) {
				System.out.println(currentSheet.getSheetName());
				for(Row row : currentSheet) {
					if(row != null) {
						row = (XSSFRow) row;
						XSSFCell cellName = (XSSFCell) row.getCell(0);
						XSSFCell cellSmiles = (XSSFCell) row.getCell(1);
						if(cellName != null && cellSmiles != null) {
							XSSFCell[] cellPair = {cellName, cellSmiles};
							if(!valueAlreadyExists(cellList, cellPair)) {
								try {
									addCellsToSheet(newSheet, lastRow, cellPair);
									cellList.add(cellPair);
									lastRow++;
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
	}
	
	public static boolean valueAlreadyExists(ArrayList<XSSFCell[]> cells, XSSFCell[] cellPair) {
		for(XSSFCell cell[] : cells) {
			boolean nameMatch = cell[0].getStringCellValue().equals(cellPair[0].getStringCellValue());
			boolean smilesMatch = cell[1].getStringCellValue().equals(cellPair[1].getStringCellValue());
			
			if(nameMatch && smilesMatch) 
				return true;
		}
		return false;
	}
	
	public static ArrayList<XSSFCell[]> getAllCells(Sheet sheet) {
		ArrayList<XSSFCell[]> cellList = new ArrayList<XSSFCell[]>();
		
		for(Row row : sheet) {
			if(row != null) {
				row = (XSSFRow) row;
				XSSFCell cellName = (XSSFCell) row.getCell(0);
				XSSFCell cellSmiles = (XSSFCell) row.getCell(1);
				XSSFCell[] cellPair = {cellName, cellSmiles};
				cellList.add(cellPair);
			}
		}
		return cellList;
	}
	
	public static void addCellsToSheet(Sheet sheet, int rowNb, XSSFCell[] cells) {
		Row row = sheet.getRow(rowNb);

		if(row == null)
			row = sheet.createRow(rowNb);
		
		Cell cellName = row.getCell(0);
		Cell cellSmiles = row.getCell(1);
		if(cellName == null) {
			cellName = row.createCell(0);
		}
		if(cellSmiles == null)
			cellSmiles = row.createCell(1);
		
		cellName.setCellValue(cells[0].getStringCellValue());
		cellSmiles.setCellValue(cells[1].getStringCellValue());
	}
}
