package Main;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;

import MolecularProperties.ChemicoPhysicalProperties;
import MolecularProperties.MolecularProperties_Chemical;
import MolecularProperties.MolecularProperties_Physical;
import fileReading.Data1D;
import fileReading.Data2D;
import fileReading.Utils;
import uk.ac.ebi.beam.*;
/**
 * Base class of all molecules
 * @author m_bla
 *
 */
public class Molecule {
	public static enum Identifier{NAME, SMILES, CONDENSED_FORMULA, SEMI_DEV_FORMULA}
	private HashMap<Identifier, String> identifiers = new HashMap<Identifier, String>();
	private float smilesValue;
	private Data1D<String> properties;
	
	/**
	 * Create a molecule from its name
	 * @param name
	 * @throws IOException 
	 * @deprecated
	 */
	@Deprecated
	public Molecule(String name) throws IOException {
		addIdentifier(Identifier.NAME, name); //Add name identifier
		addIdentifier(Identifier.SMILES, loadSmiles()); //Add SMILES identifier
		
		//Create semi-dev and condensed formula identifier from SMILES
		createSemiDevIdentifier(identifiers.get(Identifier.SMILES));
	}
	
	/**
	 * Create a molecule from either its name or SMILES equivalent
	 * @param name
	 * @param smiles
	 * @throws IOException 
	 * @deprecated
	 */
	@Deprecated
	public Molecule(String name, String smiles) throws IOException {
		addIdentifier(Identifier.NAME, name);
		addIdentifier(Identifier.SMILES, smiles);
		//Create semi-dev and condensed formula identifier from SMILES
		createSemiDevIdentifier(identifiers.get(Identifier.SMILES));
	}
	
	/**
	 * Create a molecule from either its name, SMILES or semi-dev formula
	 * @param name
	 * @param smiles
	 * @param semiDevFormula
	 * @throws IOException 
	 * @deprecated
	 */
	@Deprecated
	public Molecule(String name, String smiles, String semiDevFormula) throws IOException {
		//TODO add semi dev
		addIdentifier(Identifier.NAME, name);
		addIdentifier(Identifier.SMILES, smiles);
		addIdentifier(Identifier.SEMI_DEV_FORMULA, semiDevFormula);
		addIdentifier(Identifier.CONDENSED_FORMULA, CondensedFormula.getCondensedFormula(getIdentifier(Identifier.SEMI_DEV_FORMULA)));
		//Create semi-dev and condensed formula identifier from SMILES
		createSemiDevIdentifier(identifiers.get(Identifier.SMILES));
	}
	
	public Molecule(Data1D<String> data) {
		if(data == null)
			return;
		
		properties = data;	
		
		addIdentifier(Identifier.NAME, properties.getValue1D("Name"));
		addIdentifier(Identifier.SMILES, properties.getValue1D("SMILES"));
		addIdentifier(Identifier.SEMI_DEV_FORMULA, properties.getValue1D("Formula"));
		addIdentifier(Identifier.CONDENSED_FORMULA, properties.getValue1D("CondensedFormula"));
	}

	private void createSemiDevIdentifier(String smiles) throws IOException {
		//Create semi-dev formula identifier from SMILES
		smiles = getIdentifier(Identifier.SMILES);
		Graph g = null;
		try {
			g = Graph.fromSmiles(smiles);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Graph semiDev;
		if(g != null) {
			semiDev = Functions.expand(g);
			addIdentifier(Identifier.SEMI_DEV_FORMULA, semiDev.toSmiles());
		}
		//Create condensed-formula from semi-dev
		addIdentifier(Identifier.CONDENSED_FORMULA, CondensedFormula.getCondensedFormula(getIdentifier(Identifier.SEMI_DEV_FORMULA)));
	}
	/**
	 * Add an identifier to the identifiers of the molecule if it doesn't already exists. If so, it updates it
	 * @param key
	 * @param value
	 * @return
	 */
	public String addIdentifier(Identifier key, String value) {
		if(identifiers.containsKey(key)) {
			identifiers.remove(key);
			identifiers.put(key, value);
		}
		else {
			identifiers.put(key, value);
		}
		return value;
	}
	/**
	 * Gets the identifier of the key if it exists
	 * @param key
	 * @return the value gotten
	 */
	public String getIdentifier(Identifier key) {
		if(identifiers.containsKey(key)) {
			return identifiers.get(key);
		}
		else
			return "";
	}
	
	/**
	 * Loads the SMILES format of the molecule from its name or TODO semi-dev formula
	 * @return the load SMILES format
	 */
	public String loadSmiles() {
		//TODO Gets Smiles from beam
		if(!getIdentifier(Identifier.NAME).equals(""))
			return Resources.getSmilesFromName(getIdentifier(Identifier.NAME));
		else 
			return "";
	}
	
	/**
	 * Getter for smiles value
	 * @return SMILES value from SMILES String
	 */
	public float getSmilesValue() {
		return this.smilesValue;
	}
	
	/**
	 * TODO Calculates the SMILES value of the SMILES (molecule value)
	 */
	public void calculateSmilesValue() {
		String smiles = identifiers.get(Identifier.SMILES);
		this.smilesValue = (nbC(smiles) * 75 + nbN(smiles) * 50 + nbO(smiles) * 25 + nbP(smiles) * 15 + nbS(smiles) * 10
				+ nbI(smiles) * 5 + nbBr(smiles) * 4 + nbCl(smiles) * 3 + nbF(smiles) * 2 + nbB(smiles) * 1) / 10000;
	}
	
	/**
	 * @return the number of atom of H in the SMILES
	 */
	public int nbH(String smiles) {
		int nb = 0;
		for (int k = 0; k < smiles.length(); k++) {
			if(smiles.charAt(k)=='H') {
				nb++;
			}
		}
		return nb;
	}
	
	/**
	 * @return the number of atom of B in the SMILES
	 */
	public int nbB(String smiles) {
		int nb = 0;
		for (int k = 0; k < smiles.length(); k++) {
			if(smiles.charAt(k)=='B') {
				nb++;
			}
		}
		return nb;
	}
	
	/**
	 * @return the number of atom of C in the SMILES
	 */
	public int nbC(String smiles) {
		int nb = 0;
		for (int k = 0; k < smiles.length(); k++) {
			if(smiles.charAt(k)=='C') {
				nb++;
			}
		}
		return nb;
	}
	
	/**
	 * @return the number of atom of N in the SMILES
	 */
	public int nbN(String smiles) {
		int nb = 0;
		for (int k = 0; k < smiles.length(); k++) {
			if(smiles.charAt(k)=='N') {
				nb++;
			}
		}
		return nb;
	}
	
	/**
	 * @return the number of atom of O in the SMILES
	 */
	public int nbO(String smiles) {
		int nb = 0;
		for (int k = 0; k < smiles.length(); k++) {
			if(smiles.charAt(k)=='O') {
				nb++;
			}
		}
		return nb;
	}
	
	/**
	 * @return the number of atom of F in the SMILES
	 */
	public int nbF(String smiles) {
		int nb = 0;
		for (int k = 0; k < smiles.length(); k++) {
			if(smiles.charAt(k)=='F') {
				nb++;
			}
		}
		return nb;
	}
	
	/**
	 * @return the number of atom of P in the SMILES
	 */
	public int nbP(String smiles) {
		int nb = 0;
		for (int k = 0; k < smiles.length(); k++) {
			if(smiles.charAt(k)=='P') {
				nb++;
			}
		}
		return nb;
	}
	
	/**
	 * @return the number of atom of S in the SMILES
	 */
	public int nbS(String smiles) {
		int nb = 0;
		for (int k = 0; k < smiles.length(); k++) {
			if(smiles.charAt(k)=='S') {
				nb++;
			}
		}
		return nb;
	}
	
	/**
	 * @return the number of atom of Cl in the SMILES
	 */
	public int nbCl(String smiles) {
		int nb = 0;
		for (int k = 0; k < smiles.length()-1; k++) {
			if(smiles.substring(k,k+2).equals("Cl")) {
				nb++;
			}
		}
		return nb;
	}
	
	/**
	 * @return the number of atom of Br in the SMILES
	 */
	public int nbBr(String smiles) {
		int nb = 0;
		for (int k = 0; k < smiles.length()-1; k++) {
			if(smiles.substring(k,k+2).equals("Br")) {
				nb++;
			}
		}
		return nb;
	}
	
	/**
	 * @return the number of atom of I in the SMILES
	 */
	public int nbI(String smiles) {
		int nb = 0;
		for (int k = 0; k < smiles.length(); k++) {
			if(smiles.charAt(k)=='I') {
				nb++;
			}
		}
		return nb;
	}
	@Override
	public String toString() {
		return "Molecule [" + getIdentifier(Identifier.NAME) + " , " + getIdentifier(Identifier.SMILES) + " , " 
				+ getIdentifier(Identifier.SEMI_DEV_FORMULA) + " , " + getIdentifier(Identifier.CONDENSED_FORMULA) + "]";
	}
	
	public String toStringLong() {
		return getPropsAsString();
	}
	
	public String getPropsAsString() {
		String s = "";
		if(properties == null)
			return s;
		
		for(String v : properties.getData1D()) {
			s += v + ", ";
		}
		return s;
	}
}
