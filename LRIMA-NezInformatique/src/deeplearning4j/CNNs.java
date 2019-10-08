package deeplearning4j;

import java.util.HashMap;

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
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.AdaDelta;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.linalg.schedule.MapSchedule;
import org.nd4j.linalg.schedule.ScheduleType;

public class CNNs {
	
	public static MultiLayerNetwork initializeConvModel(int seed, int nbOfChannels, int[] nHidden, int nOut,
			HashMap<Integer, Double> learningRateSchedule, int row, int col) {
		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().seed(seed).l2(0.0005)
				.updater(new Nesterovs(new MapSchedule(ScheduleType.ITERATION, learningRateSchedule)))
				.weightInit(WeightInit.XAVIER).list()
				.layer(new ConvolutionLayer.Builder(5, 5).nIn(nbOfChannels).stride(1, 1).padding(new int[] { 0, 0 })
						.nOut(nHidden[0]).activation(Activation.IDENTITY).build())
				.layer(new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX).kernelSize(2, 2).stride(2, 2)
						.build())
				.layer(new ConvolutionLayer.Builder(5, 5).stride(1, 1).nOut(nHidden[1]).activation(Activation.IDENTITY)
						.build())
				.layer(new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
						.kernelSize(2, 2).stride(2, 2).build())
				.layer(new DenseLayer.Builder().activation(Activation.RELU).nOut(nHidden[2]).build())
				.layer(new OutputLayer.Builder(
						org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD).nOut(nOut)
								.activation(Activation.SOFTMAX).build())
				.setInputType(InputType.convolutionalFlat(row, col, nbOfChannels)).build();
		MultiLayerNetwork model = new MultiLayerNetwork(conf);
		return model;
	}
	
	public static ConvolutionLayer convInit(String name, int in, int out, int[] kernel, int[] stride, int[] pad,
			double bias) {
		return new ConvolutionLayer.Builder(kernel, stride, pad).name(name).nIn(in).nOut(out).biasInit(bias).build();
	}

	public static ConvolutionLayer conv3x3(String name, int out, double bias) {
		return new ConvolutionLayer.Builder(new int[] { 3, 3 }, new int[] { 1, 1 }, new int[] { 1, 1 }).name(name)
				.nOut(out).biasInit(bias).build();
	}

	public static ConvolutionLayer conv5x5(String name, int out, int[] stride, int[] pad, double bias) {
		return new ConvolutionLayer.Builder(new int[] { 5, 5 }, stride, pad).name(name).nOut(out).biasInit(bias)
				.build();
	}

	public static SubsamplingLayer maxPool(String name, int[] kernel) {
		return new SubsamplingLayer.Builder(kernel, new int[] { 2, 2 }).name(name).build();
	}

	public static DenseLayer fullyConnected(String name, int out, double bias, double dropOut, Distribution dist) {
		return new DenseLayer.Builder().name(name).nOut(out).biasInit(bias).dropOut(dropOut).dist(dist).build();
	}

	public static MultiLayerNetwork lenetModel(int seed, int nbOfChannels, int nLabels, int h, int w) {
		/**
		 * Revisde Lenet Model approach developed by ramgo2 achieves slightly above
		 * random Reference:
		 * https://gist.github.com/ramgo2/833f12e92359a2da9e5c2fb6333351c5
		 **/
		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().seed(seed).l2(0.005)
				.activation(Activation.RELU).weightInit(WeightInit.XAVIER)
//            .updater(new Nadam(1e-4))
				.updater(new AdaDelta()).list()
				.layer(0,
						convInit("cnn1", nbOfChannels, 50, new int[] { 5, 5 }, new int[] { 1, 1 }, new int[] { 0, 0 },
								0))
				.layer(1, maxPool("maxpool1", new int[] { 2, 2 }))
				.layer(2, conv5x5("cnn2", 100, new int[] { 5, 5 }, new int[] { 1, 1 }, 0))
				.layer(3, maxPool("maxool2", new int[] { 2, 2 })).layer(4, new DenseLayer.Builder().nOut(500).build())
				.layer(5,
						new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD).nOut(nLabels)
								.activation(Activation.SOFTMAX).build())
				.setInputType(InputType.convolutionalFlat(h, w, nbOfChannels)).build();

		return new MultiLayerNetwork(conf);

	}

	public static MultiLayerNetwork alexnetModel(int seed, int nbOfChannels, int nLabels, int h, int w) {
		/**
		 * AlexNet model interpretation based on the original paper ImageNet
		 * Classification with Deep Convolutional Neural Networks and the
		 * imagenetExample code referenced.
		 * http://papers.nips.cc/paper/4824-imagenet-classification-with-deep-convolutional-neural-networks.pdf
		 **/

		double nonZeroBias = 1;
		double dropOut = 0.5;

		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().seed(seed)
				.weightInit(new NormalDistribution(0.0, 0.01)).activation(Activation.RELU).updater(new AdaDelta())
				.gradientNormalization(GradientNormalization.RenormalizeL2PerLayer) // normalize to prevent vanishing or
																					// exploding gradients
				.l2(5 * 1e-4).list()
				.layer(convInit("cnn1", nbOfChannels, 96, new int[] { 11, 11 }, new int[] { 4, 4 }, new int[] { 3, 3 },
						0))
				.layer(new LocalResponseNormalization.Builder().name("lrn1").build())
				.layer(maxPool("maxpool1", new int[] { 3, 3 }))
				.layer(conv5x5("cnn2", 256, new int[] { 1, 1 }, new int[] { 2, 2 }, nonZeroBias))
				.layer(new LocalResponseNormalization.Builder().name("lrn2").build())
				.layer(maxPool("maxpool2", new int[] { 3, 3 })).layer(conv3x3("cnn3", 384, 0))
				.layer(conv3x3("cnn4", 384, nonZeroBias)).layer(conv3x3("cnn5", 256, nonZeroBias))
				.layer(maxPool("maxpool3", new int[] { 3, 3 }))
				.layer(fullyConnected("ffn1", 4096, nonZeroBias, dropOut, new GaussianDistribution(0, 0.005)))
				.layer(fullyConnected("ffn2", 4096, nonZeroBias, dropOut, new GaussianDistribution(0, 0.005)))
				.layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD).name("output")
						.nOut(nLabels).activation(Activation.SOFTMAX).build())
				.setInputType(InputType.convolutionalFlat(h, w, nbOfChannels)).build();

		return new MultiLayerNetwork(conf);

	}

	public static MultiLayerNetwork customModel() {
		/**
		 * Use this method to build your own custom model.
		 **/
		return null;
	}
}
