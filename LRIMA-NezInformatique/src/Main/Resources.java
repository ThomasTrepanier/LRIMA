package Main;
import java.util.ArrayList;
import java.util.HashMap;

import MolecularProperties.ChemicoPhysicalProperties;
import MolecularProperties.MolecularProperties_Chemical;
import MolecularProperties.MolecularProperties_Physical;

/**
 * Temp class until we stock our data in files (csv)
 * @author m_bla
 *
 */
public class Resources {
	private static HashMap<String, Integer> atomNumbers = new HashMap<String, Integer>();
	private static HashMap<Character, Float> smilesChar = new HashMap<Character, Float>();
	private static HashMap<String, String> namesToSmiles = new HashMap<String, String>();
	private static HashMap<String, ChemicoPhysicalProperties> chemPhysProps = new HashMap<String, ChemicoPhysicalProperties>();
	private static HashMap<String, ArrayList<String>> bruteFormulas = new HashMap<String, ArrayList<String>>();
	
	public Resources() {
		initAtomNumbers();
		initSmilesChar();
		initNamesToSmiles();
		initChemPhysProperties();
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
	public static ChemicoPhysicalProperties getChemPhysProps(String smiles) {
		return chemPhysProps.get(smiles);
	}
	
	public void initAtomNumbers() {
		atomNumbers.put("H", 1);
		atomNumbers.put("C", 12);
		atomNumbers.put("N", 14);
		atomNumbers.put("O", 16);
	}
	public void initSmilesChar() {
		
	}
	public void initNamesToSmiles() {
		namesToSmiles.put("(-)-Limonene", "CC1=CC[C@H](CC1)C(=C)C");
		namesToSmiles.put("Ethanal", "CC=O");
		namesToSmiles.put("Acetone", "CC(=O)C");
		namesToSmiles.put("Methyl", "COC=O");
		namesToSmiles.put("Ethylene", "C=C");
		namesToSmiles.put("Adamantane", "C1C2CC3CC1CC(C2)C3");
	}
	public void initChemPhysProperties() {
		chemPhysProps.put("CC1=CC[C@H](CC1)C(=C)C", new ChemicoPhysicalProperties(new MolecularProperties_Physical(), new MolecularProperties_Chemical()));
		chemPhysProps.put("CC=O", new ChemicoPhysicalProperties(new MolecularProperties_Physical(), new MolecularProperties_Chemical()));
		chemPhysProps.put("CC(=O)C", new ChemicoPhysicalProperties(new MolecularProperties_Physical(), new MolecularProperties_Chemical()));
		chemPhysProps.put("COC=O", new ChemicoPhysicalProperties(new MolecularProperties_Physical(), new MolecularProperties_Chemical()));
		chemPhysProps.put("C=C", new ChemicoPhysicalProperties(new MolecularProperties_Physical(), new MolecularProperties_Chemical()));
		chemPhysProps.put("C1C2CC3CC1CC(C2)C3", new ChemicoPhysicalProperties(new MolecularProperties_Physical(), new MolecularProperties_Chemical()));
	}
}
