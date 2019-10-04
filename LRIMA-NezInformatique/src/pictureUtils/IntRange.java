package pictureUtils;

public class IntRange {
	
	int minValue;
	int maxValue;
	
	public IntRange(int minVal, int maxVal) {
		this.minValue = minVal;
		this.maxValue = maxVal;
	}
	
	public boolean isInRange(int value) {
		if(value >= minValue && value <= maxValue)
			return true;
		return false;
	}

	@Override
	public String toString() {
		return "IntRange [minValue=" + minValue + ", maxValue=" + maxValue + "]";
	}
}
