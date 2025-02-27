package deeplearning4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;

import neural_network.TrainingData;
import pictureUtils.PictureReader;

public class FruitDatasetIterator {
	
	public static DataSet[] loadTrainingSet(int batchSize, int seed, boolean isSum) throws IOException {
		ArrayList<TrainingData>[] fruitData = PictureReader.loadFruitsDL4J(1f, isSum);
		ArrayList<TrainingData> trainData = fruitData[0];
		ArrayList<TrainingData> testData = fruitData[1];
		
		INDArray trainInputs = Nd4j.zeros(trainData.size(), trainData.get(0).getData().length);
		INDArray trainLabels = Nd4j.zeros(trainData.size(), trainData.get(0).getLabels().length);
		
		INDArray testInputs = Nd4j.zeros(testData.size(), testData.get(0).getData().length);
		INDArray testLabels = Nd4j.zeros(testData.size(), testData.get(0).getLabels().length);
		
		//Load training data
		loadData(trainData, trainInputs, trainLabels);
		
		//Load test Data
		loadData(testData, testInputs, testLabels);
		
		DataSet trainingData = new DataSet(trainInputs, trainLabels);
		DataSet evaluatingData =  new DataSet(testInputs, testLabels);
		
		trainingData.shuffle(seed);
		evaluatingData.shuffle(seed);
		
		DataSet[] trainAndTestData = {trainingData, evaluatingData};
		return trainAndTestData;
	}
	
	private static void loadData(ArrayList<TrainingData> data, INDArray inputs, INDArray labels) {
		for(int i = 0; i < data.size(); i++) {
			
			float[] sampleData = data.get(i).getData().clone(); //Gets the sample data (pixels, normalized)
			int labelIndex = getLabelIndex(data.get(i)); //Gets the index at which the label is 
			float label = data.get(i).getLabels()[labelIndex]; //Gets the label for this sample
			labels.putScalar(new int[] {i, labelIndex}, label); //Puts the label in the labels INDArray
			
			for(int j = 0; j < data.get(0).getData().length; j++) {
				inputs.putScalar(new int[] {i, j}, sampleData[j]); //Puts the pixel value in the inputs INDArray 
			}
		}
	}
	
	private static int getLabelIndex(TrainingData data) {
		boolean indexFound = false;
		int i = 0;
		int index = -1;
		while(!indexFound) {
			if(data.getLabels()[i] > 0) {
				index = i;
				indexFound = true;
			}
			i++;
		}
		return index;
	}
	//Load test set after
}

