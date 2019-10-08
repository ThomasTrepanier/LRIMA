package deeplearning4j;

import java.util.HashMap;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.schedule.MapSchedule;
import org.nd4j.linalg.schedule.ScheduleType;

public class ANNs {


	public static MultiLayerNetwork initializeFFModel(int seed, double learningRate, double momentum, int nIn, int[] nHidden, int nOut) {
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
