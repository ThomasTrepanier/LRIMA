package pictureUtils;

public class Range<E extends Comparable<E>> {
	
	
	E minValue;
	E maxValue;
	
	public Range(E minVal, E maxVal) {
		this.minValue = minVal;
		this.maxValue = maxVal;
	}
	
	public boolean isInRange(E value) {
		if(value.compareTo(minValue) > 0 && value.compareTo(maxValue) <= 0)
			return true;
		return false;
	}

	@Override
	public String toString() {
		return "Range [minValue=" + minValue + ", maxValue=" + maxValue + "]";
	}
}
