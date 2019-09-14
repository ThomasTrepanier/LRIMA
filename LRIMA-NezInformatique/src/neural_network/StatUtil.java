package neural_network;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class StatUtil {
	
	static String filePath = "Data/";
	static String savePath = "results/";
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
	
	//ACTIVATION FUNCTIONS
	//Sigmoid Function (Activation Function)
	public static float Sigmoid(float x) {
		return (float) (1f / (1f + Math.pow(Math.E, -x)));
	}
	
	//Derivate of Sigmoid Function
	public static float SigmoidDerivate(float x) {
		return Sigmoid(x) * (1.0f-Sigmoid(x));
	}
	
	public static float Relu(float x) {
		return (float) Math.max(0f, x);
	}
	public static float ReluDerivate(float x) {
		if(x > 0f)	return 1f;
		else return 0f;
	}
	
	public static float LeakyRelu(float x) {
		if(x >= 0f) return x; 	
		else return 0.01f*x;
	}
	public static float LeakyReluDerivate(float x) {
		if(x >= 0)	return 1;
		else	return 0.01f;
	}
	
	public static float Softplus(float x) {
		return (float) Math.log(1 + Math.pow(Math.E, x));
	}
	public static float SoftplusDerivate(float x) {
		return Sigmoid(x);
	}
	
	public static float Softmax(int index, Layer layer) {
		float sum = 0;
		
		for(int i = 0; i < layer.neurons.length; i++) {
			sum += Math.exp(layer.neurons[i].value);
		}
		return (float) Math.exp(layer.neurons[index].value / sum);
	}
	public static float SoftmaxDerivate(int index, Layer layer) {
		return Softmax(index, layer) * (1f - Softmax(index, layer));
	}
	
	public static float TanH(float x, float a, float b) {
		return (float) (a * Math.tanh(b * x));
	}
	public static float TanHDerivate(float x, float a, float b) {
		return (float) a*b*(1f - TanH(x, 1, b));
	}
	
	public static float lossFunction(float target, float output) {
		return output - target;
	}
	
	public static float cross_entropy(float output, float target) {
		if(output == 0)
			output = 0.01f;
		return (float) (-target*Math.log(output));
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
	
	public static float average(float[] numbers) {
		float sum = 0;
		for(float f : numbers) {
			sum += f;
		}
		return sum / numbers.length;
	}
	
	public static void loadMNIST(int setToLoad) throws IOException {
		File mnistTrain = new File(filePath + "mnist_train.csv");
		File mnistTest = new File(filePath + "mnist_test.csv");
		
		NeuralNetwork.tDataSet = loadMNISTFile(mnistTrain, setToLoad);
		NeuralNetwork.testSet = loadMNISTFile(mnistTest, setToLoad);
		
		normalizeMNIST();
		System.out.println("Loaded " + NeuralNetwork.tDataSet.size() + " training set");
		System.out.println("Loaded " + NeuralNetwork.testSet.size() + " test set");
	}
	
	public static ArrayList<TrainingData> loadMNISTFile(File file, int setToLoad) throws IOException {
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
		
		ArrayList<TrainingData> data = new ArrayList<TrainingData>();
		for(int i = 0; i < content.size(); i++) {
			String[] line = content.get(i);
			
			float[] expectedValue = getExpectedValue(line[0]);
			float[] input = new float[line.length - 1];
			for(int j = 1; j < line.length; j++) {
				input[j-1] = Float.parseFloat(line[j]);
			}
			if(data.size() - 1 < i) {
				data.add(new TrainingData(input, expectedValue));
			} else
				data.set(i, new TrainingData(input, expectedValue));
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
		ArrayList<TrainingData> data = NeuralNetwork.tDataSet;
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
	
	public static int getNbOfFiles(File file) {
		int sub_files = 0;
		if(!file.isDirectory())
			return 1;
		for(File f : file.listFiles()) {
			sub_files += getNbOfFiles(f);
		}
		return sub_files;
	}
	
	public static int getNbOfUpFolders(File file) {
		int folders = 0;
		for(File f : file.listFiles()) {
			if(f.isDirectory())
				folders++;
		}
		return folders;
	}
	
	public static void saveNeurons(Layer[] layers, float accuracy) {
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		String layersInfo = "";
		for(Layer l : layers) {
			layersInfo += l.neurons.length + "-";
		}
		String fileName = normalizeFileName(layersInfo + "_" + ts + "_" + accuracy);
		
		try {
			FileOutputStream outStream = new FileOutputStream(savePath + fileName);
			ObjectOutputStream objOutStream = new ObjectOutputStream(outStream);
			objOutStream.writeObject(layers);
			objOutStream.close();
			System.out.println("Neurones sauvegardés dans " + savePath + fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String normalizeFileName(String fileName) {
		String normalizedName = "";
		for(int i = 0; i < fileName.length(); i++) {
			char c = fileName.charAt(i);
			if(c == ' ' || c == ':') {
				normalizedName += "-";
			} else
				normalizedName += c;
		}
		return normalizedName;
	}
}
