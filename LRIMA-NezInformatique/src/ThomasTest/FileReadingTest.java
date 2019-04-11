package ThomasTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class FileReadingTest {
	
	static String folderPath = "Data/";
	static char delimiter = ';';
	public static void main(String[] args) throws IOException {
		//URL url = FileReadingTest.class.getClassLoader().getResource("test_data.csv");
		String fileName = folderPath + "test_data.csv";
		
		Scanner fileInputStream = createScanner(fileName);
		Scanner fileSecondaryStream = createScanner(fileName);
		String[][] data = loadFile(fileInputStream, fileSecondaryStream);
		print2DArray(data);
	}
	
	private static Scanner createScanner(String fileName) throws FileNotFoundException {
		File file = new File(fileName);
		Scanner fileInputStream = new Scanner(file);
		return fileInputStream;
	}
	
	private static String[][] loadFile(Scanner dataScan, Scanner lineCountScan) {
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
}
