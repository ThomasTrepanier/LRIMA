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
import ThomasTest.Data2D;
import ThomasTest.Utils;
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
	private ChemicoPhysicalProperties chemPhysProperties;
	
	/**
	 * Create a molecule from its name
	 * @param name
	 * @throws IOException 
	 */
	public Molecule(String name) throws IOException {
		addIdentifier(Identifier.NAME, name); //Add name identifier
		addIdentifier(Identifier.SMILES, loadSmiles()); //Add SMILES identifier
		
		//Create semi-dev and condensed formula identifier from SMILES
		createSemiDevIdentifier(identifiers.get(Identifier.SMILES));
		loadChemPhysProperties();
	}
	
	/**
	 * Create a molecule from either its name or SMILES equivalent
	 * @param name
	 * @param smiles
	 * @throws IOException 
	 */
	public Molecule(String name, String smiles) throws IOException {
		addIdentifier(Identifier.NAME, name);
		addIdentifier(Identifier.SMILES, smiles);
		//Create semi-dev and condensed formula identifier from SMILES
		createSemiDevIdentifier(identifiers.get(Identifier.SMILES));
		loadChemPhysProperties();
	}
	
	/**
	 * Create a molecule from either its name, SMILES or semi-dev formula
	 * @param name
	 * @param smiles
	 * @param semiDevFormula
	 * @throws IOException 
	 */
	public Molecule(String name, String smiles, String semiDevFormula) throws IOException {
		//TODO add semi dev
		addIdentifier(Identifier.NAME, name);
		addIdentifier(Identifier.SMILES, smiles);
		addIdentifier(Identifier.SEMI_DEV_FORMULA, semiDevFormula);
		addIdentifier(Identifier.CONDENSED_FORMULA, CondensedFormula.getCondensedFormula(getIdentifier(Identifier.SEMI_DEV_FORMULA)));
		//Create semi-dev and condensed formula identifier from SMILES
		createSemiDevIdentifier(identifiers.get(Identifier.SMILES));
		loadChemPhysProperties();
	}
	
	public Molecule(ArrayList<String> data, HashMap<String, Integer> indexMap) {
		ArrayList<String> properties = new ArrayList<String>();
		
		for(String s : data) {
			properties.add(s);
		}
		
		addIdentifier(Identifier.NAME, properties.get(indexMap.get("Name")));
		addIdentifier(Identifier.SMILES, properties.get(indexMap.get("SMILES")));
		addIdentifier(Identifier.SEMI_DEV_FORMULA, properties.get(indexMap.get("Formula")));
		addIdentifier(Identifier.CONDENSED_FORMULA, properties.get(indexMap.get("CondensedFormula")));
		this.chemPhysProperties = new ChemicoPhysicalProperties(properties);
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
	
	/** TODO
	 * Calculates the SMILES value of the SMILES (molecule value)
	 */
	public void calculateSmilesValue() {
		
	}
	
	/**
	 * Loads the Chemico-Physical Properties of the molecule
	 */
	public void loadChemPhysProperties() {
		//TODO Load chemPhysProps from the file (now resources)
		String smiles = getIdentifier(Identifier.SMILES);
		if(smiles == "") {
			smiles = loadSmiles();
		}
		chemPhysProperties = Resources.getChemPhysProps(smiles);
	}
	/**
	 * Get the physical properties of the molecule
	 * @return {@link MolecularProperties_Physical} physical properties
	 */
	public MolecularProperties_Physical getPhysicalProperties() {
		if(chemPhysProperties != null)
			return chemPhysProperties.getPhysProperties();
		else
			return null;
	}
	/**
	 * Get the chemical properties of the molecule
	 * @return {@link MolecularProperties_Chemical} chemical properties
	 */
	public MolecularProperties_Chemical getChemicalProperties() {
		if(chemPhysProperties != null)
			return chemPhysProperties.getChemProperties();
		else
			return null;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Molecule [" + getIdentifier(Identifier.NAME) + " , " + getIdentifier(Identifier.SMILES) + " , " 
				+ getIdentifier(Identifier.SEMI_DEV_FORMULA) + " , " + getIdentifier(Identifier.CONDENSED_FORMULA) + "]";
	}
}
