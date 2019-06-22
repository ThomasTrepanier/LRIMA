package neural_network;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class StatUtil {
	
	static String filePath = "Data/";
	//Generates a random number between min and max
	public static float RandomFloat(float min, float max) {
		return min + (float) Math.random() * (max - min);
	}
	
	public static float RandomWeight(float min, float max) {
		float a = (float) Math.random();
		float num = RandomFloat(min, max);
		
		if(a < 0.5)
			return num;
		else
			return -num;
	}
	//Sigmoid Function (Activation Function)
	public static float Sigmoid(float x) {
		return (float) (1 / (1 + Math.pow(Math.E, -x)));
	}
	
	//Derivate of Sigmoid Function
	public static float SigmoidDerivate(float x) {
		return Sigmoid(x) * (1-Sigmoid(x));
	}
	
	public static float lossFunction(float target, float output) {
		return Math.abs(target - output);
	}
	
	public static float squaredError(float output, float target) {
		return (float) (0.5 * Math.pow(2, (target-output)));
	}
	
	public static float sumSquaredError(float[] outputs, float[] targets) {
		float sum = 0;
		for(int i = 0; i < outputs.length; i++) {
			sum += squaredError(outputs[i], targets[i]);
		}
		return sum;
	}
	
	public static void loadMNIST(int setToLoad) throws IOException {
		File mnistTrain = new File(filePath + "mnist_train.csv");
		File mnistTest = new File(filePath + "mnist_test.csv");
		
		NeuralNetwork.tDataSet = loadMNISTFile(mnistTrain, setToLoad);
		NeuralNetwork.testSet = loadMNISTFile(mnistTest, setToLoad);
		
		normalizeMNIST();
		System.out.println("Loaded " + NeuralNetwork.tDataSet.length + " training set");
		System.out.println("Loaded " + NeuralNetwork.testSet.length + " test set");
	}
	
	public static TrainingData[] loadMNISTFile(File file, int setToLoad) throws IOException {
		ArrayList<String[]> content = new ArrayList<String[]>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))){
			int i = 0;
			String line = "";
			while (i < setToLoad && (line = br.readLine()) != null) {
				content.add(line.split(","));
				//System.out.println(Arrays.toString(content.get(i)));
				i++;
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			System.exit(0);
		}
		
		TrainingData[] data = new TrainingData[content.size()];
		for(int i = 0; i < content.size(); i++) {
			String[] line = content.get(i);
			
			float[] expectedValue = getExpectedValue(line[0]);
			float[] input = new float[line.length - 1];
			for(int j = 1; j < line.length; j++) {
				input[j-1] = Float.parseFloat(line[j]);
			}
			data[i] = new TrainingData(input, expectedValue);
		}
		return data;
	}
	
	private static float[] getExpectedValue(String _output) {
		float[] expectedValue = new float[10];
		float output = Float.parseFloat(_output);
		expectedValue[(int) output] = 1f;
		return expectedValue;
	}
	
	public static void normalizeMNIST() {
		TrainingData[] data = NeuralNetwork.tDataSet;
		for(TrainingData tData : data) {
			for(int i = 0; i < tData.data.length; i++) {
				tData.data[i] = tData.data[i] / 255f * 0.99f + 0.01f;
			}
		}
		NeuralNetwork.tDataSet = data;
		
		data = NeuralNetwork.testSet;
		for(TrainingData tData : data) {
			for(int i = 0; i < tData.data.length; i++) {
				tData.data[i] = tData.data[i] / 255f * 0.99f + 0.01f;
			}
		}
		NeuralNetwork.testSet = data;
	}
}
