package ThomasTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FileReader<E> {
	
	final static int ATOMIC_SYMBOL_COL = 1;
	final static int ATOMIC_NUMBER_COL = 2;
	
	public static Data2D<String> readDataFile(File file) throws InvalidFormatException, IOException{
		Workbook dataWorkbook = createWorkbook(file);
		Data2D<String> data = FileLoader.loadFileIn2D(dataWorkbook);
		DataFill.fillMissingMoleculeData(data);
		return data;
	} 
	
	public static Data2D<String> readDataFile(File file, int sheet) throws InvalidFormatException, IOException{
		Workbook dataWorkbook = createWorkbook(file);
		Data2D<String> data = FileLoader.loadFileIn2D(dataWorkbook.getSheetAt(sheet));
		DataFill.fillMissingMoleculeData(data);
		return data;
	}
	
	public static HashMap<String, Integer> readAtomsFile(File file) throws InvalidFormatException, IOException{
		Workbook dataWorkbook = createWorkbook(file);
		return FileLoader.loadHashMap(dataWorkbook, ATOMIC_SYMBOL_COL, ATOMIC_NUMBER_COL);
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
}
