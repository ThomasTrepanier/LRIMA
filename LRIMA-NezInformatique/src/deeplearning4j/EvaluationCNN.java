package deeplearning4j;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.datavec.api.io.filters.BalancedPathFilter;
import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.datavec.image.transform.FlipImageTransform;
import org.datavec.image.transform.ImageTransform;
import org.datavec.image.transform.PipelineImageTransform;
import org.datavec.image.transform.WarpImageTransform;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.primitives.Pair;

import neural_network.StatUtil;

public class EvaluationCNN {
	
	public static final String MAIN_PATH = "Data\\Fresh-Rotten";
	public static final String TRAINING_PATH = "\\train-banana";
	public static final String TEST_PATH = "\\test-banana";
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		run(0);
	}
	
	private static void run(int param) throws IOException {
		int maxPathPerLabel = 1000;
		double splitTrainTest = 0.8;
		int nRows = 100; // MNIST format
		int nCols = 100; // MNIST format
		int nbOfChannels = 3;
		int seed = 123; // Random value to shuffle the data
		int batchSize = 50;
		int nIn = nRows * nCols;
		int[] nHidden = { 20, 50, 500 };
		int epochs = 3;
		int nbFoldersTrain = StatUtil.getNbOfUpFolders(new File(MAIN_PATH + TRAINING_PATH));
		int nbFoldersTest = StatUtil.getNbOfUpFolders(new File(MAIN_PATH + TEST_PATH));

		if (nbFoldersTest != nbFoldersTrain) {
			System.out.println(
					"Numbers of fruits in train not same as in test " + nbFoldersTrain + " / " + nbFoldersTest);
			System.exit(0);
		}
		int nOut = nbFoldersTest;
		int nbLabels = nOut;
		System.out.println(nOut);
		
		System.out.println("Loading dataset...");
		DataSetIterator trainingSetIterator = null;
		DataSetIterator testSetIterator = null;
		
		/**
		 * cd Data Setup -> organize and limit data file paths: - mainPath = path to
		 * image files - fileSplit = define basic dataset split with limits on format -
		 * pathFilter = define additional file load filter to limit size and balance
		 * batch content
		 **/
		ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator();
		//System.out.println(labelMaker.getLabelForPath(PictureReader.TRAINING_FOLDER + "\\Apple Braeburn\\0_100.jpg"));
		Random rng = new Random(seed);
		File trainPath = new File(MAIN_PATH + TRAINING_PATH);
		FileSplit trainingFileSplit = new FileSplit(trainPath, NativeImageLoader.ALLOWED_FORMATS, rng);
		int trainNumExamples = (int) trainingFileSplit.length();
		System.out.println(trainNumExamples);
		BalancedPathFilter trainPathFilter = new BalancedPathFilter(rng, labelMaker, trainNumExamples, nbLabels,
				maxPathPerLabel);

		File testPath = new File(MAIN_PATH + TEST_PATH);
		FileSplit testFileSplit = new FileSplit(testPath, NativeImageLoader.ALLOWED_FORMATS, rng);
		int testNumExamples = (int) testFileSplit.length();
		System.out.println(testNumExamples);
		BalancedPathFilter testPathFilter = new BalancedPathFilter(rng, labelMaker, testNumExamples, nbLabels,
				maxPathPerLabel);
		/**
		 * Data Setup -> train test split - inputSplit = define train and test split
		 **/

		/**
		 * Data Setup -> transformation - Transform = how to tranform images and
		 * generate large dataset to train on
		 **/
		ImageTransform flipTransform1 = new FlipImageTransform(rng);
		ImageTransform flipTransform2 = new FlipImageTransform(new Random(123));
		ImageTransform warpTransform = new WarpImageTransform(rng, 42);
		boolean shuffle = false;
		List<Pair<ImageTransform, Double>> pipeline = Arrays.asList(new Pair<>(flipTransform1, 0.9),
				new Pair<>(flipTransform2, 0.8), new Pair<>(warpTransform, 0.5));
		ImageTransform transform = new PipelineImageTransform(pipeline, shuffle);
		/**
		 * Data Setup -> normalization - how to normalize images and generate large
		 * dataset to train on
		 **/
		DataNormalization scaler = new ImagePreProcessingScaler(0, 1);

		ImageRecordReader trainRR = new ImageRecordReader(nRows, nCols, nbOfChannels, labelMaker);

		ImageRecordReader testRR = new ImageRecordReader(nRows, nCols, nbOfChannels, labelMaker);
		testRR.initialize(testFileSplit);
		testSetIterator = new RecordReaderDataSetIterator(testRR, batchSize, 1, nbLabels);
		scaler.fit(testSetIterator);
		testSetIterator.setPreProcessor(scaler);

		// Train without transformations
		trainRR.initialize(trainingFileSplit, null);
		trainingSetIterator = new RecordReaderDataSetIterator(trainRR, batchSize, 1, nbLabels);
		scaler.fit(trainingSetIterator);
		trainingSetIterator.setPreProcessor(scaler);
		
		System.out.println("Configuring network...");
		MultiLayerNetwork model = CNNs.lenetModel(seed, nbOfChannels, nbLabels, nRows, nCols);
		model.init();
		System.out.println(model.summary(InputType.convolutionalFlat(nRows, nCols, nbOfChannels)));
		
		System.out.println("Evaluate untrained model...");
		Evaluation eval = ModelUtils.evaluateModel(model, testSetIterator, true);
		
		model.setListeners(new ScoreIterationListener(10));

		System.out.println("Training network...");
		long startTime = System.currentTimeMillis();
		ModelUtils.trainModel(model, trainingSetIterator, epochs);

		// Train with Transform
//		trainRR.initialize(trainingFileSplit, transform);
//		trainingSetIterator = new RecordReaderDataSetIterator(trainRR, batchSize, 1, nbLabels);
//		scaler.fit(trainingSetIterator);
//		trainingSetIterator.setPreProcessor(scaler);

		ModelUtils.trainModel(model, trainingSetIterator, epochs);

		long trainTime = (System.currentTimeMillis() - startTime) / 1000;
		System.out.println("Time to train: " + trainTime + "s");
		System.out.println("Evaluate trained model...");
		Evaluation eval2 = ModelUtils.evaluateModel(model, testSetIterator, true);
		try {
			ModelUtils.saveModelResults("Fresh-Rotten", model, eval2, trainTime, batchSize, nbLabels, nbOfChannels, epochs, false);
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ModelUtils.saveEvaluationModel(model, getAlimentInPath(), eval2.accuracy(), trainNumExamples);
		trainingSetIterator.reset();
		DataSet testDataSet = trainingSetIterator.next();
//        System.out.println(testDataSet);
		List<String> allClassLabels = trainRR.getLabels();
		int labelIndex = testDataSet.getLabels().argMax(1).getInt(0);
		System.out.println(testDataSet.getLabels());
		int[] predictedClasses = model.predict(testDataSet.getFeatures());
//        System.out.println(Arrays.toString(predictedClasses));
		String expectedResult = allClassLabels.get(labelIndex);
		String modelPrediction = allClassLabels.get(predictedClasses[0]);
		System.out.print("\nFor a single example that is labeled " + expectedResult + " the model predicted "
				+ modelPrediction + "\n\n");
	}
	
	private static String getAlimentInPath() {
		int hyphenId = TRAINING_PATH.indexOf('-');
		return TRAINING_PATH.substring(hyphenId, TRAINING_PATH.length());
	}
}
