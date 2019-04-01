import java.util.HashMap;

import MolecularProperties.ChemicoPhysicalProperties;
import MolecularProperties.MolecularProperties_Chemical;
import MolecularProperties.MolecularProperties_Physical;

public class Molecule {
	
	private HashMap<String, String> identifiers = new HashMap<String, String>();
	private float smilesValue;
	private ChemicoPhysicalProperties chemPhysProperties;
	
	/**
	 * Create a molecule from its name
	 * @param name
	 */
	public Molecule(String name) {
		addIdentifier("Name", name);
		addIdentifier("SMILES", loadSmiles());
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
				addIdentifier("Name", Resources.getNameFromSmile(smiles));
				addIdentifier("SMILES", smiles);
			}
		}
		else {
			addIdentifier("Name", name);
			if(smiles == "") {
				addIdentifier("SMILES", loadSmiles());
			}
			else
				addIdentifier("SMILES", smiles);
		}
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
				addIdentifier("Name", Resources.getNameFromSmile(smiles));
				addIdentifier("SMILES", smiles);
			}
		}
		else {
			addIdentifier("Name", name);
			if(smiles == "") {
				addIdentifier("SMILES", loadSmiles());
			}
		}
		loadChemPhysProperties();
	}
	
	/**
	 * Add an identifier to the identifiers of the molecule if it doesn't already exists. If so, it updates it
	 * @param key
	 * @param value
	 * @return
	 */
	public String addIdentifier(String key, String value) {
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
	public String getIdentifier(String key) {
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
		if(!getIdentifier("Name").equals(""))
			return Resources.getSmilesFromName(getIdentifier("Name"));
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
	 * Calculates the SMILES value of the SMILES (molecule value)
	 */
	public void calculateSmilesValue() {
		
	}
	
	/**
	 * Loads the Chemico-Physical Properties of the molecule
	 */
	public void loadChemPhysProperties() {
		//TODO Load chemPhysProps from the file (now resources)
		String smiles = getIdentifier("SMILES");
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
