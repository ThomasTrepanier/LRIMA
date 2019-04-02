package Main;
import java.util.HashMap;

import MolecularProperties.ChemicoPhysicalProperties;
import MolecularProperties.MolecularProperties_Chemical;
import MolecularProperties.MolecularProperties_Physical;

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
	 */
	public Molecule(String name) {
		addIdentifier(Identifier.NAME, name);
		addIdentifier(Identifier.SMILES, loadSmiles());
		addIdentifier(Identifier.CONDENSED_FORMULA, CondensedFormula.getCondensedFormula(getIdentifier(Identifier.SMILES)));
		loadChemPhysProperties();
		//Add SemiDev
	}
	
	/**
	 * Create a molecule from either its name or SMILES equivalent
	 * @param name
	 * @param smiles
	 */
	public Molecule(String name, String smiles) {
		if(name == "") {
			if(smiles == "") {
				System.out.println("ERROR, no name nor SMILES assigned to new Molecule");
			}
			else {
				addIdentifier(Identifier.NAME, Resources.getNameFromSmile(smiles));
				addIdentifier(Identifier.SMILES, smiles);
			}
		}
		else {
			addIdentifier(Identifier.NAME, name);
			if(smiles == "") {
				addIdentifier(Identifier.SMILES, loadSmiles());
			}
			else
				addIdentifier(Identifier.SMILES, smiles);
		}
		addIdentifier(Identifier.CONDENSED_FORMULA, CondensedFormula.getCondensedFormula(getIdentifier(Identifier.SMILES)));
		loadChemPhysProperties();
	}
	
	/**
	 * Create a molecule from either its name, SMILES or semi-dev formula
	 * @param name
	 * @param smiles
	 * @param semiDevFormula
	 */
	public Molecule(String name, String smiles, String semiDevFormula) {
		//TODO add semi dev
		if(name == "") {
			if(smiles == "") {
				System.out.println("ERROR, no name nor SMILES assigned to new Molecule");
			}
			else {
				addIdentifier(Identifier.NAME, Resources.getNameFromSmile(smiles));
				addIdentifier(Identifier.SMILES, smiles);
			}
		}
		else {
			addIdentifier(Identifier.NAME, name);
			if(smiles == "") {
				addIdentifier(Identifier.SMILES, loadSmiles());
			}
		}
		addIdentifier(Identifier.CONDENSED_FORMULA, CondensedFormula.getCondensedFormula(getIdentifier(Identifier.SMILES)));
		loadChemPhysProperties();
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
}
