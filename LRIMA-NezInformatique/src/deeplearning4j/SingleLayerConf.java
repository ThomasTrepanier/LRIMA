package deeplearning4j;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;

import java.io.IOException;

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
public class SingleLayerConf {

	public static void main(String[] args) {
		int nRows = 28; //MNIST format
		int nCols = 28; //MNIST format
		
		int seed = 123; //Random value to shuffle the data
		double learningRate = 0.005;
		double momentum = 0.75;
		int batchSize = 100;
		int epochs = 20;
		int nIn = nRows * nCols;
		int[] nHidden = {5000, 1000, 500, 300, 80};
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
		MultiLayerNetwork model = initializeModel(seed, learningRate, momentum, nIn, nHidden, nOut);
		
		model.setListeners(new ScoreIterationListener(5)); //Prints score at every X iterations
		
		System.out.println("Training model...");
		trainModel(model, trainingSet, epochs);
		
		System.out.println("Evaluate model...");
		evaluateModel(model, testSet);
	}
	
	private static MultiLayerNetwork initializeModel(int seed, double learningRate, double momentum, int nIn, int[] nHidden, int nOut) {
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
				.layer(new DenseLayer.Builder()
						.nIn(nHidden[1])
						.nOut(nHidden[2])
						.activation(Activation.RELU)
						.build())
				.layer(new DenseLayer.Builder()
						.nIn(nHidden[2])
						.nOut(nHidden[3])
						.activation(Activation.RELU)
						.build())
				.layer(new DenseLayer.Builder()
						.nIn(nHidden[3])
						.nOut(nHidden[4])
						.activation(Activation.RELU)
						.build())
				.layer(new OutputLayer.Builder()
						.nIn(nHidden[4])
						.nOut(nOut)
						.activation(Activation.SOFTMAX)
						.build())
				.build();
		
		MultiLayerNetwork model = new MultiLayerNetwork(conf);
		model.init();
		return model;
	}

	private static void trainModel(MultiLayerNetwork model, DataSet trainingSet, int epochs) {
		for(int i = 0; i < epochs; i++) {
			model.fit(trainingSet);
		}
	}
	private static void trainModel(MultiLayerNetwork model, DataSetIterator trainingSet, int epochs) {
		model.fit(trainingSet, epochs);
	}
	
	private static void evaluateModel(MultiLayerNetwork model, DataSet testSet) {
		INDArray outputs = model.output(testSet.getFeatures());
		Evaluation eval = new Evaluation();
		eval.eval(testSet.getLabels(), outputs);
		System.out.println(eval.stats());
	}
	private static void evaluateModel(MultiLayerNetwork model, DataSetIterator testSet) {
		Evaluation eval = model.evaluate(testSet);
		System.out.println(eval.stats());
	}
}
