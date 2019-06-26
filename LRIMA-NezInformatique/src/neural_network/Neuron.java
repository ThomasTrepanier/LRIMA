package neural_network;

import java.io.Serializable;

public class Neuron implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Static Var
	static float minWeightValue;
	static float maxWeightValue;
	
	//Non-Stat Var
	float[] weights;
	float[] cache_weights;
	float gradient;
	float bias;
	float value = 0;
	float valueDerivate = 0;
	
	//Constructor for hidden/output neurons
	public Neuron(float[] weights, float bias) {
		this.weights = weights;
		this.cache_weights = this.weights;
		this.bias = bias;
		this.gradient = 0;
	}
	
	//Constructor for input neurons
	public Neuron(float value) {
		this.weights = null;
		this.bias = -1;
		this.cache_weights = this.weights;
		this.gradient = -1;
		this.value = value;
	}
	
	//Static function to set min and max weights for all variables
	public static void setRangeWeights(float min, float max) {
		minWeightValue = min;
		maxWeightValue = max;
	}
	
	public void updateWeights() {
		this.weights = this.cache_weights;
	}
	
	/**
	 * Calculates the value of the neuron accordingly to its layer's activation function
	 * @param activationFunction - Layer's activation function
	 * @param x 
	 */
	public void evaluateValue(ActivationFunction activationFunction, float x, float a, float b, int index, Layer layer) {
		if(activationFunction.equals(ActivationFunction.Sigmoid)) {
			this.value = StatUtil.Sigmoid(x);
			this.valueDerivate = StatUtil.SigmoidDerivate(x);
		} else if(activationFunction.equals(ActivationFunction.Relu)) {
			this.value = StatUtil.Relu(x);
			this.valueDerivate = StatUtil.ReluDerivate(x);
		} else if(activationFunction.equals(ActivationFunction.LeakyRelu)) {
			this.value = StatUtil.LeakyRelu(x);
			this.valueDerivate = StatUtil.LeakyReluDerivate(x);
		} else if(activationFunction.equals(ActivationFunction.Softplus)) {
			this.value = StatUtil.Softplus(x);
			this.valueDerivate = StatUtil.SoftplusDerivate(x);
		} else if(activationFunction.equals(ActivationFunction.Softmax)) {
			this.value = StatUtil.Softmax(index, layer);
			this.valueDerivate = StatUtil.SoftmaxDerivate(index, layer);
		} else if(activationFunction.equals(ActivationFunction.TanH)) {
			this.value = StatUtil.TanH(x, a, b);
			this.valueDerivate = StatUtil.TanHDerivate(x, a, b);
		} else {
			this.value = StatUtil.Sigmoid(x);
			this.valueDerivate = StatUtil.SigmoidDerivate(x);
		}
	}
}
