package neural_network;

public class Layer {
	
	public Neuron[] neurons;
	
	//Constructor for hidden and output layer
	public Layer(int nbWeights, int nbNeurons) {
		this.neurons = new Neuron[nbNeurons];
		
		for(int i = 0; i < nbNeurons; i++) {
			float[] weights = new float[nbWeights];
			for(int j = 0; j < nbWeights; j++) {
				weights[j] = StatUtil.RandomFloat(Neuron.minWeightValue, Neuron.maxWeightValue);
			}
			neurons[i] = new Neuron(weights, StatUtil.RandomFloat(0, 1));
		}
	}
	
	//Constructor for input layer
	public Layer(float input[]) {
		this.neurons = new Neuron[input.length];
		for(int i = 0; i < input.length; i++) {
			this.neurons[i] = new Neuron(input[i]);
		}
	}
}
