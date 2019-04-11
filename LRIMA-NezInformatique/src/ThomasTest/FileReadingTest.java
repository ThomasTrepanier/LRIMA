package ThomasTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FileReadingTest {
	
	final static int ATOMIC_SYMBOL_COL = 1;
	final static int ATOMIC_NUMBER_COL = 2;
	//USE ARRAYLIST
	static String folderPath = "Data/";
	static char delimiter = ';';
	public static void main(String[] args) throws IOException, InvalidFormatException {
		//URL url = FileReadingTest.class.getClassLoader().getResource("test_data.csv");
		String dataName = folderPath + "test_data.csv";
		String dataExcelName = folderPath + "test_data.xlsx";
		File dataFile = new File(dataExcelName);
		
		String atomicNumberName = folderPath + "elements - Copie.xlsx";
		File atomicNumberFile = new File(atomicNumberName);
		
		//Data scan
//		Scanner fileInputStream = createScanner(dataName);
//		Data2D<String> mainData = loadFile(fileInputStream, delimiter);
		Workbook dataWorkbook = createWorkbook(dataFile);
		Data2D<String> mainData = loadFileIn2D(dataWorkbook.getSheetAt(0));
		mainData.print2DArray();
		System.out.println(mainData.getValue2D("Acetone", "SMILES"));
		
		//Atomic number scan
		Workbook wb = createWorkbook(atomicNumberFile);
		HashMap<String, Integer> atomicNumberMap = loadAtomicNumberFile(wb);
		for(String s : atomicNumberMap.keySet()) {
			System.out.println(s + " : " + atomicNumberMap.get(s));
		}
	}
	
	private static Scanner createScanner(String fileName) throws FileNotFoundException {
		File file = new File(fileName);
		Scanner fileInputStream = new Scanner(file);
		return fileInputStream;
	}
	
	private static Workbook createWorkbook(File file) throws IOException, InvalidFormatException {
		OPCPackage pkg = OPCPackage.open(file);
		XSSFWorkbook wb = new XSSFWorkbook(pkg);
		return wb;
	}
	
	public static Data1D<String> loadFile(Scanner file) {
		ArrayList<String> list = new ArrayList<String>();
		while(file.hasNext()) {
			list.add(file.next());
		}
		Data1D<String> data = new Data1D<String>(list);
		return data;
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
		ArrayList<ArrayList<String>> dataLoaded = new ArrayList<ArrayList<String>>();
		
		while(file.hasNext()) {
			ArrayList<String> nextArray = new ArrayList<String>();
			String line = file.next();
			String data = "";
			for(int i = 0; i < line.length(); i++) {
				char c = line.charAt(i);
				if(i == line.length() - 1) {
					data += c;
					nextArray.add(data);
					data = "";
				}
				else if(c == delimiter) {
					nextArray.add(data);
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
	
	public static Data2D<String> loadFileIn2D(Sheet sheet){
		ArrayList<ArrayList<String>> dataLoaded = new ArrayList<ArrayList<String>>();
		int rows = sheet.getPhysicalNumberOfRows();
		int cells = sheet.getRow(0).getPhysicalNumberOfCells();
		
		for(int r = 0; r < rows; r++) {
			ArrayList<String> line = new ArrayList<String>();
			Row row = sheet.getRow(r);
			
			if(row != null){
				for(int c = 0; c < cells; c++) {
					Cell cell = row.getCell(c);
					if(cell != null) {
						line.add(c, cell.toString());
					} else
						line.add(c, "");
				}
			}
			dataLoaded.add(line);
		}
		return new Data2D<String>(dataLoaded);
	}
	
	public static HashMap<String, Integer> loadAtomicNumberFile(Workbook wb){
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
			Cell symbolCell = row.getCell(ATOMIC_SYMBOL_COL);
			String rowSymbol = symbolCell.getStringCellValue();
			
			//Gets the atomic number from cell
			Cell numberCell = row.getCell(ATOMIC_NUMBER_COL);
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
	
	private static void printScan(Scanner scan) {
		while(scan.hasNext()) {
			System.out.println(scan.next() + ";");
		}
	}
	
}
