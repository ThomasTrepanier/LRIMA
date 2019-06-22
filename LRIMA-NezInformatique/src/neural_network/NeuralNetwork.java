package neural_network;

import java.io.IOException;

public class NeuralNetwork {

	// Layers
	static Layer[] layers; // My changes

	// Training data
	static TrainingData[] tDataSet; // My changes
	static TrainingData[] testSet;

	// Main Method
	public static void main(String[] args) {
		
//		CreateTrainingData();
//		int testAmout = 10;
//		CreateTestData(testAmout);
		
		// Create the training data
		try {
			MNIST_Loader.loadMnistDataSet();
			System.out.println(tDataSet[0]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Gets the number of inputs to initialize weights
		float nbInput = tDataSet[0].data.length;
		System.out.println(tDataSet[0].data.length);
		//(float) (-1/Math.sqrt(nbInput)), (float) (1/Math.sqrt(nbInput))
		Neuron.setRangeWeights(-1, 1);

		layers = new Layer[4];
		layers[0] = null; // Input Layer 0,2
		layers[1] = new Layer(784, 200); // Hidden Layer 2,6
		layers[2] = new Layer(200, 80); // Hidden Layer 2,6
		layers[3] = new Layer(80, 10); // Hidden Layer 2,6
		
		float percent = 1f;
		float sucessRate = 0;

		System.out.println("============");
		System.out.println("Output before training");
		System.out.println("============");
//		mnistOutput(0.01f);
		
		System.out.println("Training...");
		long trainStartTime = System.currentTimeMillis();
		train(3, 0.21f, percent);
        System.out.println("Time to train: " + (System.currentTimeMillis() - trainStartTime) + "ms");
        
		System.out.println("============");
		System.out.println("Output after training");
		System.out.println("============");
//		mnistOutput(0.01f);

		sucessRate = evaluateMNISTAccuracy() * 100;
		System.out.println("Sucess rate on test set: " + sucessRate + "%");
		
		long executionStartTime = System.nanoTime();
		forward(testSet[0].data);
		System.out.println("Execution time is " + (System.nanoTime() - executionStartTime) + "ns");
	}

	public static void CreateTrainingData() {
		float[] input1 = new float[] { 0, 0 }; // Expect 0 here
		float[] input2 = new float[] { 0, 1 }; // Expect 1 here
		float[] input3 = new float[] { 1, 0 }; // Expect 1 here
		float[] input4 = new float[] { 1, 1 }; // Expect 0 here

		float[] expectedOutput1 = new float[] { 0 };
		float[] expectedOutput2 = new float[] { 1 };
		float[] expectedOutput3 = new float[] { 1 };
		float[] expectedOutput4 = new float[] { 0 };

		// My changes (using an array for the data sets)
		tDataSet = new TrainingData[4];
		tDataSet[0] = new TrainingData(input1, expectedOutput1);
		tDataSet[1] = new TrainingData(input2, expectedOutput2);
		tDataSet[2] = new TrainingData(input3, expectedOutput3);
		tDataSet[3] = new TrainingData(input4, expectedOutput4);
	}

	public static void CreateTestData(int nbTest) {
		testSet = new TrainingData[nbTest];

		for (int i = 0; i < nbTest; i++) {
			float a = StatUtil.RandomFloat(0, 1);

			if (a < 0.25f) {
				float[] input = { 0f, 0f };
				float[] output = { 0 };
				testSet[i] = new TrainingData(input, output);
			} else if (a >= 0.25f && a < 0.5f) {
				float[] input = { 0f, 1f };
				float[] output = { 1 };
				testSet[i] = new TrainingData(input, output);
			} else if (a >= 0.5f && a < 0.75f) {
				float[] input = { 1f, 0f };
				float[] output = { 1 };
				testSet[i] = new TrainingData(input, output);
			} else {
				float[] input = { 1f, 1f };
				float[] output = { 0 };
				testSet[i] = new TrainingData(input, output);
			}
			// System.out.println(testSet[i]);
		}

	}

	public static void forward(float[] inputs) {
		// First bring the inputs into the input layer layers[0]
    	layers[0] = new Layer(inputs);
    	
        for(int i = 1; i < layers.length; i++) {
        	for(int j = 0; j < layers[i].neurons.length; j++) {
        		float sum = 0;
        		for(int k = 0; k < layers[i-1].neurons.length; k++) {
        			sum += layers[i-1].neurons[k].value*layers[i].neurons[j].weights[k];
        		}
        		//sum += layers[i].neurons[j].bias; // TODO add in the bias 
        		layers[i].neurons[j].value = StatUtil.Sigmoid(sum);
        	}
        } 	
	}

	// The idea is that you calculate a gradient and cache the updated weights in
	// the neurons.
	// When ALL the neurons new weight have been calculated we refresh the neurons.
	// Meaning we do the following:
	// Calculate the output layer weights, calculate the hidden layer weight then
	// update all the weights
	public static void backward(float learning_rate, TrainingData tData) {
    	int number_layers = layers.length;
    	int out_index = number_layers-1;
    	
    	// Update the output layers 
    	// For each output
    	for(int i = 0; i < layers[out_index].neurons.length; i++) {
    		// and for each of their weights
    		float output = layers[out_index].neurons[i].value;
    		float target = tData.expectedOutput[i];
    		float derivative = output-target;
    		float delta = derivative*(output*(1-output));
    		layers[out_index].neurons[i].gradient = delta;
    		for(int j = 0; j < layers[out_index].neurons[i].weights.length;j++) { 
    			float previous_output = layers[out_index-1].neurons[j].value;
    			float error = delta*previous_output;
    			layers[out_index].neurons[i].cache_weights[j] = layers[out_index].neurons[i].weights[j] - learning_rate*error;
    		}
    	}
    	
    	//Update all the subsequent hidden layers
    	for(int i = out_index-1; i > 0; i--) {
    		// For all neurons in that layers
    		for(int j = 0; j < layers[i].neurons.length; j++) {
    			float output = layers[i].neurons[j].value;
    			float gradient_sum = sumGradient(j,i+1);
    			float delta = (gradient_sum)*(output*(1-output));
    			layers[i].neurons[j].gradient = delta;
    			// And for all their weights
    			for(int k = 0; k < layers[i].neurons[j].weights.length; k++) {
    				float previous_output = layers[i-1].neurons[k].value;
    				float error = delta*previous_output;
    				layers[i].neurons[j].cache_weights[k] = layers[i].neurons[j].weights[k] - learning_rate*error;
    			}
    		}
    	}
    	
    	// Here we do another pass where we update all the weights
    	for(int i = 0; i< layers.length;i++) {
    		for(int j = 0; j < layers[i].neurons.length;j++) {
    			layers[i].neurons[j].updateWeights();
    		}
    	}
	}

	// This function sums up all the gradient connecting a given neuron in a given
	// layer
	public static float sumGradient(int n_index, int l_index) {
		float gradient_sum = 0;
		Layer current_layer = layers[l_index];
		for (int i = 0; i < current_layer.neurons.length; i++) {
			Neuron current_neuron = current_layer.neurons[i];
			gradient_sum += current_neuron.weights[n_index] * current_neuron.gradient;
		}
		return gradient_sum;
	}

	// This function is used to train being forward and backward.
	public static void train(int epoch, float learning_rate, float percentageOfDataSetToUse) {
		for(int i = 0; i < epoch; i++) {
    		for(int j = 0; j < tDataSet.length * percentageOfDataSetToUse; j++) {
    			forward(tDataSet[j].data);
    			backward(learning_rate,tDataSet[j]);
    		}
    	}
	}

	private static void oneOutputPass() {
		for (int i = 0; i < testSet.length; i++) {
			forward(testSet[i].data);
			float output = 0;
			float expected = 0;
			for(int j = 0; j < layers[layers.length - 1].neurons.length; j++) {
				output = layers[layers.length - 1].neurons[j].value;
				expected = testSet[i].expectedOutput[0];
			}
		
			System.out.println("Output is: " + output + " expected: " + expected);
		}
	}
	
	private static float evaluateOneOutputAccuracy(float margin) {
		float sucess = 0;
		float fail = 0;
		for (int i = 0; i < testSet.length; i++) {
			forward(testSet[i].data);
			for (int j = 0; j < layers[layers.length - 1].neurons.length; j++) { // For all neurons in final layer
				float output = layers[layers.length - 1].neurons[j].value;
				float expected = testSet[i].expectedOutput[0];
				if (Math.abs(output - expected) < margin)
					sucess++;
				else
					fail++;
			}
		}
		return sucess / (sucess + fail);
	}
	
	private static void mnistOutput(float percentToShow) {
		for (int i = 0; i < (float) testSet.length * percentToShow; i++) {
			forward(testSet[i].data);
			
			int expected = 0;
			for(int j = 0; j < testSet[i].expectedOutput.length; j++) {
				if(testSet[i].expectedOutput[j] == 1f) {
					expected = j;
					break;
				}
			}
			
			int output = 0;
			float highest = layers[layers.length - 1].neurons[0].value;
			for(int j = 1; j < layers[layers.length - 1].neurons.length; j++) {
				Neuron currentOutput = layers[layers.length - 1].neurons[j];
				if(currentOutput.value > highest) {
					highest = currentOutput.value;
					output = j;
				}
			}
			
			System.out.println("Output is: " + output + " expected: " + expected + " at " + highest + "% certainty");
		}
	}
	
	private static float evaluateMNISTAccuracy() {
		float sucess = 0;
		float fail = 0;
		for (int i = 0; i < testSet.length; i++) {
			forward(testSet[i].data);
			for (int j = 0; j < layers[layers.length - 1].neurons.length; j++) { // For all neurons in final layer
				int output = 0;
				float highest = layers[layers.length - 1].neurons[0].value;
				
				for(int k = 1; k < layers[layers.length - 1].neurons.length; k++) {
					Neuron currentOutput = layers[layers.length - 1].neurons[k];
					if(currentOutput.value > highest) {
						highest = currentOutput.value;
						output = k;
					}
				}
				
				int expected = 0;
				for(int k = 0; k < testSet[i].expectedOutput.length; k++) {
					if(testSet[i].expectedOutput[k] == 1f) {
						expected = k;
						break;
					}
				}
				
				if (output == expected)
					sucess++;
				else
					fail++;
			}
		}
		return sucess / (sucess + fail);
	}
}
