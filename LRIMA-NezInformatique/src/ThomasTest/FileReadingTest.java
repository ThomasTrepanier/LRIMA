package ThomasTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

public class FileReadingTest {
	
	final int ATOMIC_SYMBOL_COL = 1;
	final int ATOMIC_NUMBER_COL = 2;
	//USE ARRAYLIST
	static String folderPath = "Data/";
	static char delimiter = ';';
	public static void main(String[] args) throws IOException {
		//URL url = FileReadingTest.class.getClassLoader().getResource("test_data.csv");
		String dataName = folderPath + "test_data.csv";
		String atomicNumberName = folderPath + "elements.xlsx";
		File atomicNumberFile = new File(atomicNumberName);
		
		//Data scan
		Scanner fileInputStream = createScanner(dataName);
		Data2D<String> mainData = loadFile(fileInputStream, delimiter);
		mainData.print2DArray();
		System.out.println(mainData.getValue2D("Acetone", "SMILES"));
		
		Scanner atomicNumberScan = createScanner(atomicNumberName);
		printScan(atomicNumberScan);
		HashMap<String, Integer> atomicNumberMap = loadAtomicNumberFile(atomicNumberScan, delimiter);
	
		Workbook wb = new HSSFWorkbook();
	}
	
	private static Scanner createScanner(String fileName) throws FileNotFoundException {
		File file = new File(fileName);
		Scanner fileInputStream = new Scanner(file);
		return fileInputStream;
	}
	
	public static Data1D<String> loadFile(Scanner file) {
		ArrayList<String> list = new ArrayList<String>();
		while(file.hasNext()) {
			list.add(file.next());
		}
		Data1D<String> data = new Data1D<String>(list);
		return data;
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
	
	public static HashMap<String, Integer> loadAtomicNumberFile(Scanner file, char delimiter){
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		file.next();
		while(file.hasNext()) {
			String line = file.next();
			System.out.println(line);
			line.substring(0, line.indexOf(delimiter));
			System.out.println(line);
		}
		return map;
	}
	
	private static void printScan(Scanner scan) {
		while(scan.hasNext()) {
			System.out.println(scan.next() + ";");
		}
	}
	
}
