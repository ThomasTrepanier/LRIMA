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
		float a = (float) Math.random();
		float num = min + (float) (Math.random() * (max - min));
		
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
	
	public static void loadMNIST() throws IOException {
		File mnistTrain = new File(filePath + "mnist_train.csv");
		File mnistTest = new File(filePath + "mnist_test.csv");
		
		NeuralNetwork.tDataSet = loadMNISTFile(mnistTrain);
		NeuralNetwork.testSet = loadMNISTFile(mnistTest);
		
		normalizeMNIST();
		System.out.println(NeuralNetwork.tDataSet.length);
	}
	
	public static TrainingData[] loadMNISTFile(File file) throws IOException {
		ArrayList<String[]> content = new ArrayList<String[]>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))){
			int i = 0;
			String line = "";
			while (i < 10000 && (line = br.readLine()) != null) {
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
			float[] expectedValue = new float[] {Float.parseFloat(line[0])};
			float[] input = new float[line.length - 1];
			for(int j = 1; j < line.length; j++) {
				input[j-1] = Float.parseFloat(line[j]);
			}
			data[i] = new TrainingData(input, expectedValue);
		}
		return data;
	}
	
	public static void normalizeMNIST() {
		TrainingData[] data = NeuralNetwork.tDataSet;
		for(TrainingData tData : data) {
			tData.expectedOutput[0] /= 9;
			for(int i = 0; i < tData.data.length; i++) {
				tData.data[i] /= 255;
			}
		}
		NeuralNetwork.tDataSet = data;
		
		data = NeuralNetwork.testSet;
		for(TrainingData tData : data) {
			tData.expectedOutput[0] /= 9;
			for(int i = 0; i < tData.data.length; i++) {
				tData.data[i] /= 255;
			}
		}
		NeuralNetwork.testSet = data;
	}
}
