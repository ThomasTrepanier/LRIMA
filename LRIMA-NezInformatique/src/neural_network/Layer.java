package neural_network;

import java.io.Serializable;

public class Layer implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Neuron[] neurons;
	private ActivationFunction activationFunction;
	private float aParam;
	private float bParam;
	
	//Constructor for hidden and output layer
	public Layer(int nbWeights, int nbNeurons, ActivationFunction activationFunction, float a, float b) {
		this.neurons = new Neuron[nbNeurons];
		for(int i = 0; i < nbNeurons; i++) {
			float[] weights = new float[nbWeights];
			
			for(int j = 0; j < nbWeights; j++) {
				weights[j] = StatUtil.RandomFloat(Neuron.minWeightValue, Neuron.maxWeightValue);
			}
			neurons[i] = new Neuron(weights, StatUtil.RandomFloat(-1, 1));
		}
		setActivationFunction(activationFunction);
		setaParam(a);
		setbParam(b);
	}
	
	//Constructor for input layer
	public Layer(float input[]) {
		this.neurons = new Neuron[input.length];
		for(int i = 0; i < input.length; i++) {
			this.neurons[i] = new Neuron(input[i]);
		}
	}

	/**
	 * @return the activationFunction
	 */
	public ActivationFunction getActivationFunction() {
		return activationFunction;
	}

	/**
	 * @param activationFunction the activationFunction to set
	 */
	public void setActivationFunction(ActivationFunction activationFunction) {
		this.activationFunction = activationFunction;
	}

	/**
	 * @return the aParam
	 */
	public float getaParam() {
		return aParam;
	}

	/**
	 * @param aParam the aParam to set
	 */
	public void setaParam(float aParam) {
		this.aParam = aParam;
	}

	/**
	 * @return the bParam
	 */
	public float getbParam() {
		return bParam;
	}

	/**
	 * @param bParam the bParam to set
	 */
	public void setbParam(float bParam) {
		this.bParam = bParam;
	}
}
