package file_Reading;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import Main.Molecule;

public class FileReadingTest {
	
	//USE ARRAYLIST
	static String folderPath = "Data/";
	static char delimiter = ';';
	public static void main(String[] args) throws IOException, InvalidFormatException {
//		String dataName = folderPath + "test_data.csv";
		String dataExcelName = folderPath + "test_data.xlsx";
		File dataFile = new File(dataExcelName);
		
		String atomicNumberName = folderPath + "atoms.xlsx";
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
		
		for(String s : mainData.getIndexMap().keySet()) {
			System.out.print(s + ", ");
		}
		System.out.println();
//		Molecule aldehyde = new Molecule(mainData.getLine("Aldehyde"), mainData.getIndexMap());
//		System.out.println("\n" + aldehyde);
//		
//		Molecule ethylene = new Molecule(mainData.getLine("Ethylene"), mainData.getIndexMap());
//		System.out.print(ethylene + "\n" + ethylene.getIdentifier(Identifier.SMILES));
		
		Molecule Adamantane = new Molecule(mainData.getLine("xyz"));
		System.out.println("\n" + Adamantane);
		System.out.println(Adamantane.toStringLong());
	}
	
	private static void printScan(Scanner scan) {
		while(scan.hasNext()) {
			System.out.println(scan.next() + ";");
		}
	}
}
