package deeplearning4j;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;

import java.io.IOException;
import java.util.HashMap;

import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.graph.MergeVertex;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
public class FFNetwork {

	public static void main(String[] args) {
		int nRows = 28; //MNIST format
		int nCols = 28; //MNIST format
		
		int seed = 123; //Random value to shuffle the data
		double learningRate = 0.005;
		double momentum = 0.75;
		int batchSize = 200;
		int epochs = 100;
		int nIn = nRows * nCols;
		int[] nHidden = {32, 16};
		int nOut = 10;
		
		System.out.println("Loading dataset...");
		DataSetIterator trainingSetIterator = null;
		DataSetIterator testSetIterator = null;
		DataSet[] fruitData = null;
		DataSet trainingSet = null;
		DataSet testSet = null;
		try {
			fruitData = FruitDatasetIterator.loadTrainingSet(batchSize, seed, false);
			trainingSet = fruitData[0];
			testSet = fruitData[1];
			nIn = trainingSet.getFeatures().columns();
			nOut = trainingSet.getLabels().columns();
			System.out.println("IN: " + nIn + " OUT: " + nOut);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*try {
			trainingSetIterator = new MnistDataSetIterator(batchSize, true, seed);
			testSetIterator = new MnistDataSetIterator(batchSize, true, seed);
			nIn = trainingSetIterator.inputColumns();
			nOut = trainingSetIterator.totalOutcomes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		System.out.println("Configuring network...");
		MultiLayerNetwork model = initializeFFModel(seed, learningRate, momentum, nIn, nHidden, nOut);
		model.addListeners(new ScoreIterationListener(5)); //Prints score at every X iterations
		
		System.out.println("Training model...");
		ModelUtils.trainModel(model, trainingSet, epochs);
		
		System.out.println("Evaluate model...");
		ModelUtils.evaluateModel(model, testSet, true);
	}
	
	private static MultiLayerNetwork initializeFFModel(int seed, double learningRate, double momentum, int nIn, int[] nHidden, int nOut) {
		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
				.seed(seed)
				.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
				.updater(new Nesterovs(learningRate, momentum))
				.weightInit(WeightInit.XAVIER)
				.list()
				.layer(new DenseLayer.Builder()
						.nIn(nIn)
						.nOut(nHidden[0])
						.activation(Activation.RELU)
						.build())
				.layer(new DenseLayer.Builder()
						.nIn(nHidden[0])
						.nOut(nHidden[1])
						.activation(Activation.RELU)
						.build())
				.layer(new OutputLayer.Builder()
						.nIn(nHidden[1])
						.nOut(nOut)
						.activation(Activation.SOFTMAX)
						.build())
				.build();
		
		MultiLayerNetwork model = new MultiLayerNetwork(conf);
		model.init();
		return model;
	}
}
