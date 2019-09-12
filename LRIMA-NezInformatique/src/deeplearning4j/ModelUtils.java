package deeplearning4j;

import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

public class ModelUtils {

	public static void trainModel(MultiLayerNetwork model, DataSet trainingSet, int epochs) {
		for(int i = 0; i < epochs; i++) {
			model.fit(trainingSet);
		}
	}
	public static void trainModel(MultiLayerNetwork model, DataSetIterator trainingSet, int epochs) {
		model.fit(trainingSet, epochs);
	}
	
	public static void evaluateModel(MultiLayerNetwork model, DataSet testSet) {
		INDArray outputs = model.output(testSet.getFeatures());
		Evaluation eval = new Evaluation();
		eval.eval(testSet.getLabels(), outputs);
		System.out.println(eval.stats());
	}
	public static void evaluateModel(MultiLayerNetwork model, DataSetIterator testSet) {
		Evaluation eval = model.evaluate(testSet);
		System.out.println(eval.stats());
	}
}
