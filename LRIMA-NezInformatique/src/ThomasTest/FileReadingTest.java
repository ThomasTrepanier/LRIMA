package ThomasTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class FileReadingTest {
	
	//USE ARRAYLIST
	static String[][] data;
	static HashMap<String, Integer> dataIdMap = new HashMap<String, Integer>();
	static String folderPath = "Data/";
	static char delimiter = ';';
	public static void main(String[] args) throws IOException {
		//URL url = FileReadingTest.class.getClassLoader().getResource("test_data.csv");
		String dataName = folderPath + "test_data.csv";
		
		Scanner fileInputStream = createScanner(dataName);
		Scanner fileSecondaryStream = createScanner(dataName);
		data = loadData(fileInputStream, fileSecondaryStream);
		initializeDataIdMap(data[0], dataIdMap);
		print2DArray(data);
		System.out.println(getValue("Adamantane", "SMILES"));
	}
	
	private static Scanner createScanner(String fileName) throws FileNotFoundException {
		File file = new File(fileName);
		Scanner fileInputStream = new Scanner(file);
		return fileInputStream;
	}
	
	public static void loadFile(Scanner file, ArrayList<String> destination) {
		String[] data;
		int elemCount = 0;
	}
	public static void loadFile(Scanner file, ArrayList<String> destinationX, ArrayList<String> destinationY) {
		
	}
	private static String[][] loadData(Scanner dataScan, Scanner lineCountScan) {
		//TODO add error detection
		//Fill in the data array with the file data
		int linesCount = findLinesCount(lineCountScan);
		String line = "";
		if(dataScan.hasNext()) {
			line = dataScan.next();
		}
		int colCount = findColCount(line, delimiter);
		String[][] data = new String[linesCount][colCount];
		int lineAt = 0;
		while(dataScan.hasNext()) {
			if(lineAt != 0)
				line = dataScan.next();
			String dataRead = "";
			int colAt = 0;
			for(int i = 0; i < line.length(); i++) {
				char charRead = line.charAt(i);
				if(charRead == delimiter || i == line.length() - 1) {
					if(i == line.length() - 1) //if at the end of the line, need to add it too
						dataRead += line.charAt(i);
					data[lineAt][colAt] = dataRead;
					colAt++;
					dataRead = "";
				}
				else
					dataRead += charRead;
			}
			lineAt++;
		}
		return data;
	}
	
	private static void initializeDataIdMap(String[] dataLine, HashMap<String, Integer> dataMap) {
		for(int i = 0; i < dataLine.length; i++) {
			dataMap.put(dataLine[i], i);
		}
	}
	private static int findColCount(String line, char delimiter) {
		int count = 0;
		for(int i = 0; i < line.length(); i++) {
			if(line.charAt(i) == delimiter)
				count++;
		}
		return count + 1;
	}
	
	private static int findLinesCount(Scanner scan) {
		int count = 0;
		while(scan.hasNext()) {
			scan.next();
			count++;
		}
		scan.reset();
		return count;
	}
	
	private static void print2DArray(String[][] array) {
		for(int i = 0; i < array.length; i++) {
			System.out.print("[");
			for(int j = 0; j < array[0].length; j++) {
				System.out.print(array[i][j]);
				if(j != array[0].length - 1)
					System.out.print(",");
			}
			System.out.println("]");
		}
	}
	
	private static String getValue(String moleculeIdentifier, String key) {
		//Identifier is name now
		int valueIndex = 0;
		if(dataIdMap.containsKey(key)) {
			valueIndex = dataIdMap.get(key);
		} else {
			System.out.println("Mauvaise key pour getValue");
			return null;
		}
		
		for(int i = 0 ; i < data.length; i++) {
			if(data[i][0].equals(moleculeIdentifier)) {
				return data[i][valueIndex];
			}
		}
		System.out.println("Couldn't find identifier");
		return null;
	}
}
