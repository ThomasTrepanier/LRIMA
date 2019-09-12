package neural_network;
import java.io.IOException;
import java.util.ArrayList;

public class MNIST_Loader {

    public static void loadMnistDataSet() throws IOException {
        MnistMatrix[] mnistMatrix = new MnistDataReader().readData("mnist_data/train-images.idx3-ubyte", "mnist_data/train-labels.idx1-ubyte");
        
        NeuralNetwork.tDataSet = loadDataIn(mnistMatrix);
        
        //printMnistMatrix(mnistMatrix[mnistMatrix.length - 1]);
        mnistMatrix = new MnistDataReader().readData("mnist_data/t10k-images.idx3-ubyte", "mnist_data/t10k-labels.idx1-ubyte");
        
        NeuralNetwork.testSet = loadDataIn(mnistMatrix);
        //printMnistMatrix(mnistMatrix[0]);
        StatUtil.normalizeMNIST();
    }
    
    private static ArrayList<TrainingData> loadDataIn(MnistMatrix[] matrixArray) {
        int size = matrixArray[0].getNumberOfColumns()*matrixArray[0].getNumberOfRows();
        ArrayList<TrainingData> tData = new ArrayList<TrainingData>();
        
        for(int i = 0; i < matrixArray.length; i++) {
        	MnistMatrix matrix = matrixArray[i];
        	
            float[] data = new float[size];
            for(int j = 0; j < matrix.getNumberOfColumns(); j++) {
            	for(int k = 0; k < matrix.getNumberOfRows(); k++) {
            		data[j*matrix.getNumberOfRows() + k] = matrix.getValue(j, k);
            	}
            }
            float[] expectedValue = new float[10];
            expectedValue[matrix.getLabel()] = 1f;
            
            tData.set(i, new TrainingData(data, expectedValue));
        }
        return tData;
    }
    private static void printMnistMatrix(final MnistMatrix matrix) {
        System.out.println("label: " + matrix.getLabel());
        for (int r = 0; r < matrix.getNumberOfRows(); r++ ) {
            for (int c = 0; c < matrix.getNumberOfColumns(); c++) {
                System.out.print(matrix.getValue(r, c) + " ");
            }
            System.out.println();
        }
    }
}
