package deeplearning4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

import neural_network.Layer;
import neural_network.StatUtil;
import neural_network.TrainingData;

public class ModelUtils {

	static final String SAVE_PATH = "results\\";
	static final String RESULTS_FILE = "DL4J-Results.xlsx";

	public static void trainModel(MultiLayerNetwork model, DataSet trainingSet, int epochs) {
		for (int i = 0; i < epochs; i++) {
			model.fit(trainingSet);
		}
	}

	public static void trainModel(MultiLayerNetwork model, DataSetIterator trainingSet, int epochs) {
		model.fit(trainingSet, epochs);
	}

	public static Evaluation evaluateModel(MultiLayerNetwork model, DataSet testSet, boolean showMatrix) {
		INDArray outputs = model.output(testSet.getFeatures());
		Evaluation eval = new Evaluation();
		eval.eval(testSet.getLabels(), outputs);
		System.out.println(eval.stats(true, showMatrix));
		return eval;
	}

	public static Evaluation evaluateModel(MultiLayerNetwork model, DataSetIterator testSet, boolean showMatrix) {
		Evaluation eval = model.evaluate(testSet);
		System.out.println(eval.stats(true, showMatrix));
		return eval;
	}

	public static void saveModel(MultiLayerNetwork model, int nOut, int[] nHidden, double accuracy) throws IOException {
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		String layersInfo = "";
		for (int layer : nHidden) {
			layersInfo += layer;
		}
		String fileName = StatUtil.normalizeFileName(layersInfo + "_" + ts + "_" + accuracy);

		File locationToSave = new File(SAVE_PATH + fileName + ".zip"); // Where to save the network. Note: the file is
																		// in .zip format - can be opened externally
		boolean saveUpdater = true; // Updater: i.e., the state for Momentum, RMSProp, Adagrad etc. Save this if you
									// want to train your network more in the future
		model.save(locationToSave, saveUpdater);
	}

	public static void saveModelResults(String sheetName, MultiLayerNetwork model, Evaluation eval, long trainTime,
			int batchSize, int nbOfLabels, int channels, int epochs, boolean usedTransform)
			throws FileNotFoundException, IOException, EncryptedDocumentException, InvalidFormatException {
		File file = new File(SAVE_PATH + RESULTS_FILE);
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		double accuracy = eval.accuracy();
		double learning_rate;
		try {
			learning_rate = model.getLearningRate(0);
		} catch (NullPointerException e) {
			learning_rate = 0;
		}
		int nbOfLayers = model.getnLayers();

		System.out.println("Writing... " + sheetName);
		try (InputStream inp = new FileInputStream(file)) {
			Workbook workbook = WorkbookFactory.create(inp);
			XSSFSheet sheet = (XSSFSheet) workbook.getSheet(sheetName);
			if (sheet == null) {
				sheet = (XSSFSheet) workbook.createSheet();
			}

			if (sheet.getRow(0) == null)
				initializeHeaders(sheet);

			int startRow = sheet.getPhysicalNumberOfRows();
			Row row = sheet.createRow(startRow);
			Cell cell1 = row.createCell(0);
			cell1.setCellValue(channels);
			Cell cell2 = row.createCell(1);
			cell2.setCellValue(nbOfLabels);
			Cell cell3 = row.createCell(2);
			cell3.setCellValue(nbOfLayers);
			Cell cell4 = row.createCell(3);
			cell4.setCellValue(learning_rate);
			Cell cell5 = row.createCell(4);
			cell5.setCellValue(batchSize);
			Cell cell6 = row.createCell(5);
			cell6.setCellValue(epochs);
			Cell cell7 = row.createCell(6);
			cell7.setCellValue(accuracy);
			Cell cell8 = row.createCell(7);
			cell8.setCellValue(trainTime);
			Cell cell9 = row.createCell(8);
			cell9.setCellValue(usedTransform);

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
		}

	}

	private static void initializeHeaders(XSSFSheet sheet) {
		Row row = sheet.createRow(0);
		Cell cell1 = row.createCell(0);
		cell1.setCellValue("Number of channels");
		Cell cell2 = row.createCell(1);
		cell2.setCellValue("Number of labels");
		Cell cell3 = row.createCell(2);
		cell3.setCellValue("Number of layers");
		Cell cell4 = row.createCell(3);
		cell4.setCellValue("Learning rate");
		Cell cell5 = row.createCell(4);
		cell5.setCellValue("Batch Size");
		Cell cell6 = row.createCell(5);
		cell6.setCellValue("Epochs");
		Cell cell7 = row.createCell(6);
		cell7.setCellValue("Accuracy");
		Cell cell8 = row.createCell(7);
		cell8.setCellValue("Train Time");
		Cell cell9 = row.createCell(8);
		cell9.setCellValue("Used Transform");
	}

	public static boolean saveEvaluationModel(MultiLayerNetwork model, String aliment, double accuracy, int examples) {
		String name = "evaluate-" + aliment + "-" + accuracy + "-" + examples;

		File locationToSave = new File(SAVE_PATH + name + ".zip"); // Where to save the network. Note: the file is in
																	// .zip format - can be opened externally
		boolean saveUpdater = true; // Updater: i.e., the state for Momentum, RMSProp, Adagrad etc. Save this if you
									// want to train your network more in the future
		try {
			model.save(locationToSave, saveUpdater);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public static MultiLayerNetwork loadModel(ModelJob job, String aliment, boolean loadUpdater) {
		File savedModel = getModelFile(job, aliment);
		if(savedModel != null) {
			try {
				return MultiLayerNetwork.load(savedModel, loadUpdater);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private static File getModelFile(ModelJob job, String aliment) {
		String modelName = getSavedName(job, aliment);
		System.out.println(modelName);
		if(modelName.equals("")) {
			return null;
		}
		
		File parentFolder = new File(SAVE_PATH);
		for(File f : parentFolder.listFiles()) {
			if(f.getName().contains(modelName))
				return f;
		}
		return null;
	}
	private static String getSavedName(ModelJob job, String aliment) {
		if(job.equals(ModelJob.Regonize))
			return "recognize";
		else if(job.equals(ModelJob.Evaluate))
			return "evaluate-" + aliment;
		else
			return "";
	}
}
