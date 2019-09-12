package deeplearning4j;

import java.io.IOException;
import java.util.HashMap;

import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ops.LossFunction;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.schedule.MapSchedule;
import org.nd4j.linalg.schedule.ScheduleType;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class ConvolutionnalNetwork {

	public static void main(String[] args) {
		int nRows = 28; //MNIST format
		int nCols = 28; //MNIST format
		int nbOfChannels = 1;
		int seed = 123; //Random value to shuffle the data
		int batchSize = 50;
		int epochs = 5;
		int nIn = nRows * nCols;
		int[] nHidden = {20, 50, 500};
		int nOut = 10;
		
		System.out.println("Loading dataset...");
		DataSetIterator trainingSetIterator = null;
		DataSetIterator testSetIterator = null;
		DataSet[] fruitData = null;
		DataSet trainingSet = null;
		DataSet testSet = null;
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
		
		try {
			trainingSetIterator = new MnistDataSetIterator(batchSize, true, seed);
			testSetIterator = new MnistDataSetIterator(batchSize, true, seed);
			nIn = trainingSetIterator.inputColumns();
			nOut = trainingSetIterator.totalOutcomes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HashMap<Integer, Double> learningRateSchedule = new HashMap<>();
		learningRateSchedule.put(0, 0.06);
		learningRateSchedule.put(200,  0.05);
		learningRateSchedule.put(600, 0.028);
		learningRateSchedule.put(800, 0.006);
		learningRateSchedule.put(1000, 0.001);
		
		System.out.println("Configuring network...");
		MultiLayerNetwork model = initializeConvModel(seed, nbOfChannels, nHidden, nOut, learningRateSchedule, nRows, nCols);
		
		model.setListeners(new ScoreIterationListener(10));
		
		ModelUtils.trainModel(model, trainingSetIterator, epochs);
		
		System.out.println("Evaluate model...");
		ModelUtils.evaluateModel(model, testSetIterator);
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
		model.init();
		return model;
	}
}
