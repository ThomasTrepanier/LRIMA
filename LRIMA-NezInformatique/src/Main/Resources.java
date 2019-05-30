package Main;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Temp class until we stock our data in files (csv)
 * @author m_bla
 *
 */
public class Resources {
	private static HashMap<String, Integer> atomNumbers = new HashMap<String, Integer>();
	private static HashMap<Character, Float> smilesChar = new HashMap<Character, Float>();
	private static HashMap<String, String> namesToSmiles = new HashMap<String, String>();
	private static HashMap<String, ArrayList<String>> isomeresFromCondensed = new HashMap<String, ArrayList<String>>();
	
	public Resources() {
		initAtomNumbers();
		initSmilesChar();
		initNamesToSmiles();
		initIsomeresFromCondensed();
		//-,=,#,$,@,@@,\,/,(,),[,]
	}
	public static int getAtomNumber(String s) {
		return atomNumbers.get(s);
	}
	public static float getSmilesChar(char c) {
		return smilesChar.get(c);
	}
	public static String getSmilesFromName(String name) {
		return namesToSmiles.get(name);
	}
	public static String getNameFromSmile(String smiles) {
		for(String k : namesToSmiles.keySet()) {
			if(namesToSmiles.get(k).equals(smiles)) {
				return k;
			}
		}
		return "";
	}
	
	private void initAtomNumbers() {
		atomNumbers.put("H", 1);
		atomNumbers.put("B", 5);
		atomNumbers.put("C", 6);
		atomNumbers.put("N", 7);
		atomNumbers.put("O", 8);
		atomNumbers.put("F", 9);
		atomNumbers.put("N", 7);
		atomNumbers.put("O", 8);
		atomNumbers.put("F", 9);
		atomNumbers.put("C", 12);
		atomNumbers.put("P", 15);
		atomNumbers.put("S", 16);
		atomNumbers.put("Cl", 17);
		atomNumbers.put("Br", 35);
		atomNumbers.put("I", 53);
	}
	private void initSmilesChar() {
		
	}
	private void initNamesToSmiles() {
		namesToSmiles.put("(-)-Limonene", "CC1=CC[C@H](CC1)C(=C)C");
		namesToSmiles.put("Ethanal", "CC=O");
		namesToSmiles.put("Acetone", "CC(=O)C");
		namesToSmiles.put("Methyl", "COC=O");
		namesToSmiles.put("Ethylene", "C=C");
		namesToSmiles.put("Adamantane", "C1C2CC3CC1CC(C2)C3");
	}
	private void initIsomeresFromCondensed() {
	}
}
