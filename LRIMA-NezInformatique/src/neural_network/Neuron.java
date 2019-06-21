package neural_network;

public class Neuron {
	
	//Static Var
	static float minWeightValue;
	static float maxWeightValue;
	
	//Non-Stat Var
	float[] weights;
	float[] cache_weights;
	float gradient;
	float bias;
	float value = 0;
	
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
}
