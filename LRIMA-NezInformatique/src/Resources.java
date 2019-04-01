import java.util.HashMap;

public class Resources {
	private static HashMap<String, Integer> atomNumbers = new HashMap<String, Integer>();
	private static HashMap<Character, Float> smilesChar = new HashMap<Character, Float>();
	
	public Resources() {
		atomNumbers.put("H", 1);
		atomNumbers.put("C", 12);
		atomNumbers.put("N", 14);
		atomNumbers.put("O", 16);
		//-,=,#,$,@,@@,\,/,(,),[,]
	}
	public int getAtomNumber(String s) {
		return atomNumbers.get(s);
	}
	public float getSmilesChar(char c) {
		return smilesChar.get(c);
	}
}
