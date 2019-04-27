package fileReading;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Class that stores a 2 dimensionnal array of information
 * First line should be the index map
 * In pracitce, stores an {@link ArrayList} of {@link Data1D} 
 * @author Thomas Trepanier
 *
 * @param <E>
 */
public class Data2D<E> {
	
	/**
	 * {@link HashMap} containing a value as a key and the index of that value in the array
	 */
	private HashMap<E, Integer> colIndexMap;
	
	/**
	 * {@link ArrayList} containing the list of {@link Data1D} 
	 */
	private ArrayList<Data1D<E>> data2D;
	
	/**
	 * Initialize an array with an existing dataset
	 * @param data
	 */
	public Data2D(ArrayList<Data1D<E>> data) {
		data2D = data;
		this.colIndexMap = new HashMap<E, Integer>();
		for(int i = 0; i < data2D.get(0).getDataSize(); i++) {
			this.colIndexMap.put(data2D.get(0).getValue1D(i), i);
		}
		
		for(int i = 1; i < data2D.get(0).getDataSize(); i++) {
			data2D.get(i).setIndexMap(this.colIndexMap);
		}
	}
	
	/**
	 * Gets the line at the specified index
	 * @param id - Index to get line at
	 * @return {@link Data1D} representing the line
	 */
	public Data1D<E> getLine(int id){
		return data2D.get(id);
	}
	/**
	 * Gets the line containing the specified identifier (I.E NAME, SMILES, FORMULA)
	 * @param lineIdentifier - Value contained in the line to identify it
	 * @return {@link Data1D} representing the line
	 */
	public Data1D<E> getLine(E lineIdentifier){
		for(Data1D<E> list : data2D) {
			for(E value : list.getData1D()) {
				if(value.equals(lineIdentifier))
					return list;
			}
		}
		System.out.println("La ligne n'a pas été trouvée");
		return null;
	}
	
	/**
	 * Gets a value at the specified line index and column index
	 * @param lineId - Line index
	 * @param colId - Column index
	 * @return Value in the array
	 */
	public E getValue2D(int lineId, int colId) {
		return data2D.get(lineId).getValue1D(colId);
	}
	/**
	 * Gets a value at the specified line identifier and column index
	 * @param lineId - Line index
	 * @param colIdentifier - Value representing the column
	 * @return Value in the array
	 */
	public E getValue2D(int lineId, E colIdentifier) {
		return data2D.get(lineId).getValue1D(colIdentifier, colIndexMap);
	}
	/**
	 * Gets a value at the specified line and column identifier
	 * @param lineIdentifier - Value representing the line
	 * @param colIdentifier - Value representing the column
	 * @return Value in the array
	 */
	public E getValue2D(E lineIdentifier, E colIdentifier) {
		int ligId = 0;
		int colId = colIndexMap.get(colIdentifier);
		
		for(int i = 0; i < data2D.size(); i++) {
			if(data2D.get(i).getValue1D(0).equals(lineIdentifier)) {
				ligId = i;
				break;
			}
		}
		return data2D.get(ligId).getValue1D(colId);
	}
	
	/**
	 * Gets the {@link ArrayList} of {@link Data1D} 
	 * @return The dataset
	 */
	public ArrayList<Data1D<E>> getData(){
		return data2D;
	}
	/**
	 * Gets the index map for this data
	 * @return {@link HashMap} of the index map
	 */
	public HashMap<E, Integer> getIndexMap(){
		return this.colIndexMap;
	}
	
	/**
	 * Gets the index of a column identified by the value
	 * @param value - Column identifier
	 * @return Index
	 */
	public int getColIndex(E value) {
		return colIndexMap.get(value);
	}
	
	/**
	 * Prints the dataset in the console
	 */
	public void print2DArray() {
		if(data2D == null) {
			System.out.println("2D data array is null");
			return;
		}
		System.out.print("[");
		for(Data1D<E> list : data2D) {
			System.out.print("[");
			for(E value : list.getData1D()) {
				System.out.print(value + ";");
			}
			System.out.println("]");
		}
	}
	
	public void writeInExcelFile(String file, String sheetName) throws IOException, InvalidFormatException {
		try(InputStream inp = new FileInputStream(file)){
			Workbook workbook = WorkbookFactory.create(inp);
			XSSFSheet sheet = (XSSFSheet) workbook.createSheet(sheetName);
			
			int rowNum = 0;
			for(int i = 0; i < data2D.size(); i++) {
				Row row = sheet.createRow(rowNum++);
				int colNum = 0;
				Data1D<E> line = data2D.get(i);
				for(int j = 0; j < line.getDataSize(); j++) {
					Cell cell = row.createCell(colNum++);
					E element = line.getValue1D(j);
					cell.setCellValue(element.toString());
				}
			}
			try {
	            FileOutputStream outputStream = new FileOutputStream(file);
	            workbook.write(outputStream);
	            workbook.close();
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
	}
}
