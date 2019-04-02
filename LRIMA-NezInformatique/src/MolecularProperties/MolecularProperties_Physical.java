package MolecularProperties;

public class MolecularProperties_Physical {
	private float molecularWeight;
	private float extractMass; //In g/mol (Maybe create a type CONCENTRATION for that
	private float monoisotopicMass; //In g/mol (Maybe create a type CONCENTRATION for that
	/**
	 * @return the molecularWeight
	 */
	public float getMolecularWeight() {
		return molecularWeight;
	}
	/**
	 * @param molecularWeight the molecularWeight to set
	 */
	public void setMolecularWeight(float molecularWeight) {
		this.molecularWeight = molecularWeight;
	}
	/**
	 * @return the extractMass
	 */
	public float getExtractMass() {
		return extractMass;
	}
	/**
	 * @param extractMass the extractMass to set
	 */
	public void setExtractMass(float extractMass) {
		this.extractMass = extractMass;
	}
	/**
	 * @return the monoisotopicMass
	 */
	public float getMonoisotopicMass() {
		return monoisotopicMass;
	}
	/**
	 * @param monoisotopicMass the monoisotopicMass to set
	 */
	public void setMonoisotopicMass(float monoisotopicMass) {
		this.monoisotopicMass = monoisotopicMass;
	}
}
