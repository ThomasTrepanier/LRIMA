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

import Main.CondensedFormula;
import uk.ac.ebi.beam.Functions;
import uk.ac.ebi.beam.Graph;
public class FileReadingTest {
	
	//USE ARRAYLIST
	static String folderPath = "Data/";
	static char delimiter = ';';
	public static void main(String[] args) throws IOException, InvalidFormatException {
//		String dataName = folderPath + "test_data.csv";
		String dataExcelName = folderPath + "test_data.xlsx";
		File dataFile = new File(dataExcelName);
		
		String atomicNumberName = folderPath + "elements - Copie.xlsx";
		File atomicNumberFile = new File(atomicNumberName);
		
		//Data scan
//		Scanner fileInputStream = createScanner(dataName);
//		Data2D<String> mainData = loadFile(fileInputStream, delimiter);
		Data2D<String> mainData = FileReader.readDataFile(dataFile);
		
		mainData.print2DArray();
		System.out.println("SMILES of acetone: " + mainData.getValue2D("Acetone", "SMILES"));
		
		//Atomic number scan
		HashMap<String, Integer> atomicNumberMap = FileReader.readAtomsFile(atomicNumberFile);
		
		System.out.println("O: " + atomicNumberMap.get("O"));
	}
	
	private static void printScan(Scanner scan) {
		while(scan.hasNext()) {
			System.out.println(scan.next() + ";");
		}
	}
}
