package file_Reading;

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
	
	//Column index containing the symbols in the file
	final static int ATOMIC_SYMBOL_COL = 0;
	//Column index containing the atomic number in the file
	final static int ATOMIC_NUMBER_COL = 1;
	
	/**
	 * Reads and fills a {@link Data2D} with the data in the Excel File. Fills missing information
	 * @param file - File to read
	 * @return {@link Data2D} containing all the data in the file
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public static Data2D<String> readDataFile(File file) throws InvalidFormatException, IOException{
		Workbook dataWorkbook = createWorkbook(file);
		Data2D<String> data = FileLoader.loadFileIn2D(dataWorkbook);
		//DataFill.fillMissingMoleculeData(data);
		return data;
	} 
	
	/**
	 * Reads and fills a {@link Data2D} with the data in the specified sheet of the Excel File. Fills missing information
	 * @param file - File to read
	 * @return {@link Data2D} containing all the data in the file
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public static Data2D<String> readDataFile(File file, int sheet) throws InvalidFormatException, IOException{
		Workbook dataWorkbook = createWorkbook(file);
		Data2D<String> data = FileLoader.loadFileIn2D(dataWorkbook.getSheetAt(sheet));
		//DataFill.fillMissingMoleculeData(data);
		return data;
	}
	
	public static Data2D<String> readDataFile(File file, String sheetName) throws InvalidFormatException, IOException{
		Workbook dataWorkbook = createWorkbook(file);
		Data2D<String> data = FileLoader.loadFileIn2D(dataWorkbook.getSheet(sheetName));
		//DataFill.fillMissingMoleculeData(data);
		return data;
	}
	
	/**
	 * Creates a {@link HashMap} containing the atomic symbol and the equivalent atomic number of all atoms
	 * @param file - File containing the information
	 * @return HashMap containing the Symbol-Atomic Number map
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public static HashMap<String, Integer> readAtomsFile(File file) throws InvalidFormatException, IOException{
		Workbook dataWorkbook = createWorkbook(file);
		return FileLoader.loadHashMap(dataWorkbook, ATOMIC_SYMBOL_COL, ATOMIC_NUMBER_COL);
	}
	
	/**
	 * Probably useless
	 * @deprecated 
	 */
	@Deprecated
	private static Scanner createScanner(String fileName) throws FileNotFoundException {
		File file = new File(fileName);
		Scanner fileInputStream = new Scanner(file);
		return fileInputStream;
	}
	
	/**
	 * Creates a readable {@link Workbook} from the Excel File
	 * @param file - Excel File
	 * @return {@link Workbook} that we can read
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public static Workbook createWorkbook(File file) throws IOException, InvalidFormatException {
		OPCPackage pkg = OPCPackage.open(file);
		XSSFWorkbook wb = new XSSFWorkbook(pkg);
		return wb;
	}
}
