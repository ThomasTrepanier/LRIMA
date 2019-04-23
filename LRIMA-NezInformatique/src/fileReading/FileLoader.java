package fileReading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;

public class FileLoader {
	
	public static Data1D<String> loadFile(Scanner file) {
		ArrayList<String> list = new ArrayList<String>();
		while(file.hasNext()) {
			list.add(file.next());
		}
		Data1D<String> data = new Data1D<String>(list);
		return data;
	}
	
	public static Data1D<String> loadFileIn1D(Workbook wb){
		Iterator<Sheet> it = wb.iterator();
		Sheet currentSheet = null;
		ArrayList<String> list = new ArrayList<String>();
		
		while(it.hasNext()) {
			currentSheet = it.next();
			for(Row row : currentSheet) {
				if(row != null) {
					for(Cell cell : row) {
						if(cell != null) {
							String cellContent = cell.getStringCellValue();
							list.add(cellContent);
						}
					}
				}
			}
		}
		return new Data1D<String>(list);
	}
	public static Data1D<String> loadFileIn1D(Sheet sheet){
		ArrayList<String> list = new ArrayList<String>();
		
		for(Row row : sheet) {
			if(row != null) {
				for(Cell cell : row) {
					if(cell != null) {
						String cellContent = cell.getStringCellValue();
						list.add(cellContent);
					}
				}
			}
		}
		return new Data1D<String>(list);
	}
	public static Data2D<String> loadFile(Scanner file, char delimiter) {
		ArrayList<Data1D<String>> dataLoaded = new ArrayList<Data1D<String>>();
		
		while(file.hasNext()) {
			Data1D<String> nextArray = new Data1D<String>();
			String line = file.next();
			String data = "";
			for(int i = 0; i < line.length(); i++) {
				char c = line.charAt(i);
				if(i == line.length() - 1) {
					data += c;
					nextArray.getData1D().add(data);
					data = "";
				}
				else if(c == delimiter) {
					nextArray.addValue(data);
					data = "";
				} else {
					data += c;
				}
			}
			dataLoaded.add(nextArray);
		}
		Data2D<String> data = new Data2D<String>(dataLoaded);
		return data;
	}
	
	public static Data2D<String> loadFileIn2D(Workbook wb){
		Iterator<Sheet> it = wb.sheetIterator();
		ArrayList<Data1D<String>> dataLoaded = new ArrayList<Data1D<String>>();
		Sheet currentSheet = null;
		
		while(it.hasNext()) {
			currentSheet = it.next();
			int rows = currentSheet.getPhysicalNumberOfRows();
			int cells = currentSheet.getRow(0).getPhysicalNumberOfCells();
			
			for(int r = 0; r < rows; r++) {
				Data1D<String> line = new Data1D<String>();
				Row row = currentSheet.getRow(r);
				
				if(row != null){
					for(int c = 0; c < cells; c++) {
						Cell cell = row.getCell(c);
						if(cell != null) {
							line.addValueAtRange(c, cell.toString());
						} else
							line.addValueAtRange(c, "");;
					}
					dataLoaded.add(line);
				}
			}
		}
		return new Data2D<String>(dataLoaded);
	}
	public static Data2D<String> loadFileIn2D(Sheet sheet){
		ArrayList<Data1D<String>> dataLoaded = new ArrayList<Data1D<String>>();
		int rows = sheet.getPhysicalNumberOfRows();
		int cells = sheet.getRow(0).getPhysicalNumberOfCells();
		
		for(int r = 0; r < rows; r++) {
			Data1D<String> line = new Data1D<String>();
			Row row = sheet.getRow(r);
			
			if(row != null){
				for(int c = 0; c < cells; c++) {
					Cell cell = row.getCell(c);
					if(cell != null) {
						line.addValueAtRange(c, cell.toString());
					} else
						line.addValueAtRange(c, "");
				}
				dataLoaded.add(line);
			}
		}
		return new Data2D<String>(dataLoaded);
	}
	
	public static HashMap<String, Integer> loadHashMap(Workbook wb, int keyCol, int valueCol){
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		int sheetAmount = wb.getNumberOfSheets();
		if(sheetAmount < 0)
			return null;
		
		//Gets the first sheet
		Sheet workingSheet = wb.getSheetAt(0);
		XSSFRow row = null;
		int rowCount = workingSheet.getPhysicalNumberOfRows();
		
		for(int r = 1; r < rowCount; r++) {
			row = (XSSFRow) workingSheet.getRow(r);
			
			//Gets the Atomic Symbol from cell
			Cell symbolCell = row.getCell(keyCol);
			String rowSymbol = symbolCell.getStringCellValue();
			
			//Gets the atomic number from cell
			Cell numberCell = row.getCell(valueCol);
			int rowAtomicNumber;
			//Finds appropriate function depending on the cell type
			if(numberCell.getCellTypeEnum().equals(CellType.NUMERIC)) {
				rowAtomicNumber = (int) numberCell.getNumericCellValue();
			} else if(numberCell.getCellTypeEnum().equals(CellType.STRING)) {
				rowAtomicNumber = Integer.parseInt(numberCell.getStringCellValue());
			} else {
				rowAtomicNumber = 0;
			}
			//Puts the value in map
			map.put(rowSymbol, rowAtomicNumber);
		}
		return map;
	}
}
