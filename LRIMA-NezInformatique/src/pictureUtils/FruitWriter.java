package pictureUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import file_Reading.Data1D;
import neural_network.StatUtil;
import neural_network.TrainingData;

//TODO: DATA VISUALIZER
public class FruitWriter {
	
	static final String DATA_FOLDER = "Data\\";
	static final String TRAINING_FOLDER = "Data\\Fruit_Training\\";
	static final String TEST_FOLDER = "Data\\Fruit_Test\\";
	static final String FILE = "FruitDatasets.xlsx";
	static final String TRAINING_SHEET_SUM = "Training Set Summed";
	static final String TRAINING_SHEET_IND = "Training Set Individual";
	static final String TEST_SHEET_SUM = "Test Set Summed";
	static final String TEST_SHEET_IND = "Test Set Individual";
	
	public static void writeFruitsToCSV() throws IOException {
		File trainingFile = new File(TRAINING_FOLDER);
		File testFile = new File(TEST_FOLDER);
		int nbOfTrainingFolders = StatUtil.getNbOfUpFolders(trainingFile);
		int nbOfTestFolders = StatUtil.getNbOfUpFolders(testFile);
		
		System.out.println("Writing training sum...");
		writeFolder(nbOfTrainingFolders, trainingFile, TRAINING_SHEET_SUM, true);
		System.out.println("Writing training indi...");
		writeFolder(nbOfTrainingFolders, trainingFile, TRAINING_SHEET_IND, false);
		trainingFile = null;
		
		System.out.println("Writing test sum...");
		writeFolder(nbOfTestFolders, testFile, TEST_SHEET_SUM, true);
		System.out.println("Writing test indi...");
		writeFolder(nbOfTestFolders, testFile, TEST_SHEET_IND, false);
		
//		System.out.println("Loading Data Summed...");
//		TrainingData[][] dataSummed = PictureReader.loadFruitsDL4J(1f, true);	
//		
//		try {
//			//writeInExcelFile(DATA_FOLDER + FILE, TRAINING_SHEET_SUM, dataSummed[0]);
//			dataSummed[0] = null;
//			//writeInExcelFile(DATA_FOLDER + FILE, TEST_SHEET_SUM, dataSummed[1]);
//			
//		} catch (InvalidFormatException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		dataSummed = null;
//		
//		System.out.println("Loading Data Individualized...");
//		TrainingData[][] dataInd = PictureReader.loadFruitsDL4J(1f, false);
//		try {
//			//writeInExcelFile(DATA_FOLDER + FILE, TRAINING_SHEET_IND, dataInd[0]);
//			dataInd[0] = null;
//			//writeInExcelFile(DATA_FOLDER + FILE, TEST_SHEET_IND, dataInd[1]);
//		} catch (InvalidFormatException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		System.out.println("DONE");
	}
	
	private static void writeFolder(int nbOfFolders, File parentFile, String sheetName, boolean isSum) throws IOException {
		String oriSheetName = sheetName;
		int finishedAtRow = 0;
		for(int i = 0; i < nbOfFolders; i++) {
			File currentFruitFile = parentFile.listFiles()[i];
			sheetName = oriSheetName + " - " + currentFruitFile.getName();
			System.out.println("Current fruit file: " + currentFruitFile);
			ArrayList<TrainingData> currentFruitData = PictureReader.loadFruitsFromFolder(currentFruitFile.getPath(), isSum);
			try {
				finishedAtRow = writeInExcelFile(DATA_FOLDER + FILE, sheetName, (TrainingData[]) currentFruitData.toArray(), finishedAtRow);
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			currentFruitData = null;
			currentFruitFile = null;
		}
	}
	private static int writeInExcelFile(String file, String sheetName, TrainingData[] data, int rowStart) throws IOException, InvalidFormatException {
		System.out.println("Writing... " + sheetName);
		try(InputStream inp = new FileInputStream(file)){
			SXSSFWorkbook workbook = new SXSSFWorkbook(100);
			SXSSFSheet sheet =  workbook.getSheet(sheetName);
			System.out.println(sheet);
			if(sheet == null) {
				sheet =  workbook.createSheet(sheetName);
			}
				
			System.out.println(rowStart);
			int rowNum = rowStart; 
			for(int i = 0; i < data.length; i++) {
				TrainingData current = data[i];
				Row row = sheet.createRow(rowStart + i);
				rowNum++;
				//System.out.println("Row: " + (i+1));
				int colNum = 0;
				//System.out.println(current);
				for(int j = 0; j < current.getLabels().length; j++) {
					Cell cell = row.createCell(colNum++);
					//System.out.println("Col: " + colNum);
					cell.setCellValue(current.getLabels()[j]);
					cell = null;
				}
				for(int k = 0; k < current.getData().length; k++) {
					Cell cell = row.createCell(colNum++);
					//System.out.println("Col: " + colNum);
					cell.setCellValue(current.getData()[k]);
					cell = null;
				}
				row = null;
				current = null;
			}
//			Cell cell = row.createCell(colNum++);
//			E element = line.getValue1D(j);
//			cell.setCellValue(element.toString());
		
			try {
	            FileOutputStream outputStream = new FileOutputStream(file);
	            workbook.write(outputStream);
	            workbook.close();
	            outputStream.close();
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			return 0; //CHANGE TO ROWSTART IF NEEDED
		}
	}

}
