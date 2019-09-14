package deeplearning4j;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.datavec.api.io.filters.BalancedPathFilter;
import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.api.split.InputSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.datavec.image.transform.FlipImageTransform;
import org.datavec.image.transform.ImageTransform;
import org.datavec.image.transform.PipelineImageTransform;
import org.datavec.image.transform.WarpImageTransform;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.GradientNormalization;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.distribution.Distribution;
import org.deeplearning4j.nn.conf.distribution.GaussianDistribution;
import org.deeplearning4j.nn.conf.distribution.NormalDistribution;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.LocalResponseNormalization;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ops.LossFunction;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.learning.config.AdaDelta;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.schedule.MapSchedule;
import org.nd4j.linalg.schedule.ScheduleType;
import org.nd4j.tools.BTools;

import neural_network.StatUtil;

import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.linalg.primitives.Pair;

public class ConvolutionnalNetwork {
	
	static final String MAIN_PATH = "Data\\Fruits";
	static final String TRAINING_FOLDER = "Data\\Fruits\\Fruit_Training\\";
	static final String TEST_FOLDER = "Data\\Fruits\\Fruit_Test\\";
	
	public static void main(String[] args) throws IOException {
		int maxPathPerLabel = 1000;
		double splitTrainTest = 0.8;
		int nRows = 100; //MNIST format
		int nCols = 100; //MNIST format
		int nbOfChannels = 3;
		int seed = 123; //Random value to shuffle the data
		int batchSize = 50;
		int epochs = 5;
		int nIn = nRows * nCols;
		int[] nHidden = {20, 50, 500};
		int nOut = StatUtil.getNbOfUpFolders(new File(TRAINING_FOLDER));
		int nbLabels = nOut;
		System.out.println(nOut);
		System.out.println("Loading dataset...");
		DataSetIterator trainingSetIterator = null;
		DataSetIterator testSetIterator = null;
		DataSet[] fruitData = null;
		DataSet trainingSet = null;
		DataSet testSet = null;

		/**cd
         * Data Setup -> organize and limit data file paths:
         *  - mainPath = path to image files
         *  - fileSplit = define basic dataset split with limits on format
         *  - pathFilter = define additional file load filter to limit size and balance batch content
         **/
		ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator();
		Random rng = new Random(seed);
		File trainPath = new File(TRAINING_FOLDER);
		FileSplit trainingFileSplit = new FileSplit(trainPath, NativeImageLoader.ALLOWED_FORMATS, rng);
		int trainNumExamples = (int) trainingFileSplit.length();
		System.out.println(trainNumExamples);
		BalancedPathFilter trainPathFilter = new BalancedPathFilter(rng, labelMaker, trainNumExamples, nbLabels, maxPathPerLabel);
		
		File testPath = new File(TEST_FOLDER);
		FileSplit testFileSplit = new FileSplit(testPath, NativeImageLoader.ALLOWED_FORMATS, rng);
		int testNumExamples = (int) testFileSplit.length();
		System.out.println(testNumExamples);
		BalancedPathFilter testPathFilter = new BalancedPathFilter(rng, labelMaker, testNumExamples, nbLabels, maxPathPerLabel);
		/**
         * Data Setup -> train test split
         *  - inputSplit = define train and test split
         **/

		/**
         * Data Setup -> transformation
         *  - Transform = how to tranform images and generate large dataset to train on
         **/
		ImageTransform flipTransform1 = new FlipImageTransform(rng);
		ImageTransform flipTransform2 = new FlipImageTransform(new Random(123));
		ImageTransform warpTransform = new WarpImageTransform(rng, 42);
		boolean shuffle = false;
		List<Pair<ImageTransform, Double>> pipeline = Arrays.asList(new Pair<>(flipTransform1, 0.9),
																	new Pair<>(flipTransform2, 0.8),
																	new Pair<>(warpTransform, 0.5));
		ImageTransform transform = new PipelineImageTransform(pipeline, shuffle);
		/**
         * Data Setup -> normalization
         *  - how to normalize images and generate large dataset to train on
         **/
		DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
		
		ImageRecordReader trainRR = new ImageRecordReader(nRows, nCols, nbOfChannels, labelMaker);

		ImageRecordReader testRR = new ImageRecordReader(nRows, nCols, nbOfChannels, labelMaker);
		testRR.initialize(testFileSplit);
		testSetIterator = new RecordReaderDataSetIterator(testRR, batchSize, 1, nbLabels);
		scaler.fit(testSetIterator);
		testSetIterator.setPreProcessor(scaler);
		
		//Train without transformations
		trainRR.initialize(trainingFileSplit, null);
		trainingSetIterator = new RecordReaderDataSetIterator(trainRR, batchSize, 1, nbLabels);
		scaler.fit(trainingSetIterator);
		trainingSetIterator.setPreProcessor(scaler);
//		try {
//			fruitData = FruitDatasetIterator.loadTrainingSet(batchSize, seed, false);
//			trainingSet = fruitData[0];
//			testSet = fruitData[1];
//			nIn = trainingSet.getFeatures().columns();
//			nOut = trainingSet.getLabels().columns();
//			System.out.println("IN: " + nIn + " OUT: " + nOut);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		/*try {
			trainingSetIterator = new MnistDataSetIterator(batchSize, true, seed);
			testSetIterator = new MnistDataSetIterator(batchSize, true, seed);
			nIn = trainingSetIterator.inputColumns();
			nOut = trainingSetIterator.totalOutcomes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		HashMap<Integer, Double> learningRateSchedule = new HashMap<>();
		learningRateSchedule.put(0, 0.06);
		learningRateSchedule.put(200,  0.05);
		learningRateSchedule.put(600, 0.028);
		learningRateSchedule.put(800, 0.006);
		learningRateSchedule.put(1000, 0.001);
		
		System.out.println("Configuring network...");
		MultiLayerNetwork model = lenetModel(seed, nbOfChannels, nbLabels, nRows, nCols);
		model.init();
		System.out.println(model.summary(InputType.convolutionalFlat(nRows, nCols, nbOfChannels)));
		
		model.setListeners(new ScoreIterationListener(10));
		
		System.out.println("Training network...");
		long startTime = System.currentTimeMillis();
		ModelUtils.trainModel(model, trainingSetIterator, epochs);
		
//		trainRR.initialize(trainData, transform);
//		trainingSetIterator = new RecordReaderDataSetIterator(trainRR, batchSize, 1, nbLabels);
//		scaler.fit(trainingSetIterator);
//		trainingSetIterator.setPreProcessor(scaler);
//		
//		ModelUtils.trainModel(model, trainingSetIterator, epochs);
		
		long trainTime = (System.currentTimeMillis() - startTime) / 1000;
		System.out.println("Time to train: " + trainTime + "s");
		System.out.println("Evaluate model...");
		Evaluation eval = ModelUtils.evaluateModel(model, testSetIterator, true);
		try {
			ModelUtils.saveModelResults(model, eval, trainTime, batchSize, nbLabels, nbOfChannels);
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ModelUtils.saveModel(model, nOut, nHidden, eval.accuracy());
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
        System.out.print("\nFor a single example that is labeled " + expectedResult + " the model predicted " + modelPrediction + "\n\n");
	}
	
	private static MultiLayerNetwork initializeConvModel(int seed, int nbOfChannels, int[] nHidden, int nOut, HashMap<Integer, Double> learningRateSchedule, int row, int col) {
		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
				.seed(seed)
				.l2(0.0005)
				.updater(new Nesterovs(new MapSchedule(ScheduleType.ITERATION, learningRateSchedule)))
				.weightInit(WeightInit.XAVIER)
				.list()
				.layer(new ConvolutionLayer.Builder(5, 5)
						.nIn(nbOfChannels)
						.stride(1, 1)
						.padding(new int[] {0,0})
						.nOut(nHidden[0])
						.activation(Activation.IDENTITY)
						.build())
				.layer(new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
						.kernelSize(2, 2)
						.stride(2, 2)
						.build())
				.layer(new ConvolutionLayer.Builder(5, 5)
						.stride(1, 1)
						.nOut(nHidden[1])
						.activation(Activation.IDENTITY)
						.build())
				.layer(new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
						.kernelSize(2, 2)
						.stride(2, 2)
						.build())
				.layer(new DenseLayer.Builder()
						.activation(Activation.RELU)
						.nOut(nHidden[2])
						.build())
				.layer(new OutputLayer.Builder(org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
						.nOut(nOut)
						.activation(Activation.SOFTMAX)
						.build())
				.setInputType(InputType.convolutionalFlat(row, col, nbOfChannels))
				.build();
		MultiLayerNetwork model = new MultiLayerNetwork(conf);
		return model;
	}

    private static ConvolutionLayer convInit(String name, int in, int out, int[] kernel, int[] stride, int[] pad, double bias) {
        return new ConvolutionLayer.Builder(kernel, stride, pad).name(name).nIn(in).nOut(out).biasInit(bias).build();
    }

    private static ConvolutionLayer conv3x3(String name, int out, double bias) {
        return new ConvolutionLayer.Builder(new int[]{3,3}, new int[] {1,1}, new int[] {1,1}).name(name).nOut(out).biasInit(bias).build();
    }

    private static ConvolutionLayer conv5x5(String name, int out, int[] stride, int[] pad, double bias) {
        return new ConvolutionLayer.Builder(new int[]{5,5}, stride, pad).name(name).nOut(out).biasInit(bias).build();
    }

    private static SubsamplingLayer maxPool(String name,  int[] kernel) {
        return new SubsamplingLayer.Builder(kernel, new int[]{2,2}).name(name).build();
    }

    private static DenseLayer fullyConnected(String name, int out, double bias, double dropOut, Distribution dist) {
        return new DenseLayer.Builder().name(name).nOut(out).biasInit(bias).dropOut(dropOut).dist(dist).build();
    }

    public static MultiLayerNetwork lenetModel(int seed, int nbOfChannels, int nLabels, int h, int w) {
        /**
         * Revisde Lenet Model approach developed by ramgo2 achieves slightly above random
         * Reference: https://gist.github.com/ramgo2/833f12e92359a2da9e5c2fb6333351c5
         **/
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
            .seed(seed)
            .l2(0.005)
            .activation(Activation.RELU)
            .weightInit(WeightInit.XAVIER)
//            .updater(new Nadam(1e-4))
            .updater(new AdaDelta())
            .list()
            .layer(0, convInit("cnn1", nbOfChannels, 50 ,  new int[]{5, 5}, new int[]{1, 1}, new int[]{0, 0}, 0))
            .layer(1, maxPool("maxpool1", new int[]{2,2}))
            .layer(2, conv5x5("cnn2", 100, new int[]{5, 5}, new int[]{1, 1}, 0))
            .layer(3, maxPool("maxool2", new int[]{2,2}))
            .layer(4, new DenseLayer.Builder().nOut(500).build())
            .layer(5, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                .nOut(nLabels)
                .activation(Activation.SOFTMAX)
                .build())
            .setInputType(InputType.convolutionalFlat(h, w, nbOfChannels))
            .build();

        return new MultiLayerNetwork(conf);

    }

    public static MultiLayerNetwork alexnetModel(int seed, int nbOfChannels, int nLabels, int h, int w) {
        /**
         * AlexNet model interpretation based on the original paper ImageNet Classification with Deep Convolutional Neural Networks
         * and the imagenetExample code referenced.
         * http://papers.nips.cc/paper/4824-imagenet-classification-with-deep-convolutional-neural-networks.pdf
         **/

        double nonZeroBias = 1;
        double dropOut = 0.5;

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
            .seed(seed)
            .weightInit(new NormalDistribution(0.0, 0.01))
            .activation(Activation.RELU)
            .updater(new AdaDelta())
            .gradientNormalization(GradientNormalization.RenormalizeL2PerLayer) // normalize to prevent vanishing or exploding gradients
            .l2(5 * 1e-4)
            .list()
            .layer(convInit("cnn1", nbOfChannels, 96, new int[]{11, 11}, new int[]{4, 4}, new int[]{3, 3}, 0))
            .layer(new LocalResponseNormalization.Builder().name("lrn1").build())
            .layer(maxPool("maxpool1", new int[]{3,3}))
            .layer(conv5x5("cnn2", 256, new int[] {1,1}, new int[] {2,2}, nonZeroBias))
            .layer(new LocalResponseNormalization.Builder().name("lrn2").build())
            .layer(maxPool("maxpool2", new int[]{3,3}))
            .layer(conv3x3("cnn3", 384, 0))
            .layer(conv3x3("cnn4", 384, nonZeroBias))
            .layer(conv3x3("cnn5", 256, nonZeroBias))
            .layer(maxPool("maxpool3", new int[]{3,3}))
            .layer(fullyConnected("ffn1", 4096, nonZeroBias, dropOut, new GaussianDistribution(0, 0.005)))
            .layer(fullyConnected("ffn2", 4096, nonZeroBias, dropOut, new GaussianDistribution(0, 0.005)))
            .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                .name("output")
                .nOut(nLabels)
                .activation(Activation.SOFTMAX)
                .build())
            .setInputType(InputType.convolutionalFlat(h, w, nbOfChannels))
            .build();

        return new MultiLayerNetwork(conf);

    }

    public static MultiLayerNetwork customModel() {
        /**
         * Use this method to build your own custom model.
         **/
        return null;
    }
}
