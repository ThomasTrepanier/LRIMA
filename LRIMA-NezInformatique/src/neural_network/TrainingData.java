package neural_network;

import java.util.Arrays;

public class TrainingData extends Data {

	float[] expectedOutput;
	
	public TrainingData(float[] data, float[] expectedOutput) {
		super(data);
		this.expectedOutput = expectedOutput;
	}
	
	public float[] getLabels() {
		return expectedOutput;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TrainingData [expectedOutput=" + Arrays.toString(expectedOutput) + ", data=" + Arrays.toString(data)
				+ "]";
	}
}
