
public class ChemicoPhysicalProperties {
	private float molecularWeight;
	private float xLogP3AA;
	private int hydrogenBondDonorCount;
	private int hydrogenBondAcceptorCount;
	private int rotatableBondCount;
	private float extractMass; //In g/mol (Maybe create a type CONCENTRATION for that
	private float monoisotopicMass; //In g/mol (Maybe create a type CONCENTRATION for that
	private float topologicalPolarSurfaceArea;
	private int heavyAtomCount;
	private int formalCharge;
	private int complexity;
	private int isotopeAtomCount;
	private int definedAtomStereocenterCount;
	private int undefinedAtomStereacenterCount;
	private int definedBondStereocenterCount;
	private int undefinedBondStereocenterCount;
	private int covalentlyBondedUnitCount;
	private boolean isCompoundCanonicalizable;
	
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
	 * @return the xLogP3AA
	 */
	public float getxLogP3AA() {
		return xLogP3AA;
	}
	/**
	 * @param xLogP3AA the xLogP3AA to set
	 */
	public void setxLogP3AA(float xLogP3AA) {
		this.xLogP3AA = xLogP3AA;
	}
	/**
	 * @return the hydrogenBondDonorCount
	 */
	public int getHydrogenBondDonorCount() {
		return hydrogenBondDonorCount;
	}
	/**
	 * @param hydrogenBondDonorCount the hydrogenBondDonorCount to set
	 */
	public void setHydrogenBondDonorCount(int hydrogenBondDonorCount) {
		this.hydrogenBondDonorCount = hydrogenBondDonorCount;
	}
	/**
	 * @return the hydrogenBondAcceptorCount
	 */
	public int getHydrogenBondAcceptorCount() {
		return hydrogenBondAcceptorCount;
	}
	/**
	 * @param hydrogenBondAcceptorCount the hydrogenBondAcceptorCount to set
	 */
	public void setHydrogenBondAcceptorCount(int hydrogenBondAcceptorCount) {
		this.hydrogenBondAcceptorCount = hydrogenBondAcceptorCount;
	}
	/**
	 * @return the rotatableBondCount
	 */
	public int getRotatableBondCount() {
		return rotatableBondCount;
	}
	/**
	 * @param rotatableBondCount the rotatableBondCount to set
	 */
	public void setRotatableBondCount(int rotatableBondCount) {
		this.rotatableBondCount = rotatableBondCount;
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
	/**
	 * @return the topologicalPolarSurfaceArea
	 */
	public float getTopologicalPolarSurfaceArea() {
		return topologicalPolarSurfaceArea;
	}
	/**
	 * @param topologicalPolarSurfaceArea the topologicalPolarSurfaceArea to set
	 */
	public void setTopologicalPolarSurfaceArea(float topologicalPolarSurfaceArea) {
		this.topologicalPolarSurfaceArea = topologicalPolarSurfaceArea;
	}
	/**
	 * @return the heavyAtomCount
	 */
	public int getHeavyAtomCount() {
		return heavyAtomCount;
	}
	/**
	 * @param heavyAtomCount the heavyAtomCount to set
	 */
	public void setHeavyAtomCount(int heavyAtomCount) {
		this.heavyAtomCount = heavyAtomCount;
	}
	/**
	 * @return the formalCharge
	 */
	public int getFormalCharge() {
		return formalCharge;
	}
	/**
	 * @param formalCharge the formalCharge to set
	 */
	public void setFormalCharge(int formalCharge) {
		this.formalCharge = formalCharge;
	}
	/**
	 * @return the complexity
	 */
	public int getComplexity() {
		return complexity;
	}
	/**
	 * @param complexity the complexity to set
	 */
	public void setComplexity(int complexity) {
		this.complexity = complexity;
	}
	/**
	 * @return the isotopeAtomCount
	 */
	public int getIsotopeAtomCount() {
		return isotopeAtomCount;
	}
	/**
	 * @param isotopeAtomCount the isotopeAtomCount to set
	 */
	public void setIsotopeAtomCount(int isotopeAtomCount) {
		this.isotopeAtomCount = isotopeAtomCount;
	}
	/**
	 * @return the definedAtomStereocenterCount
	 */
	public int getDefinedAtomStereocenterCount() {
		return definedAtomStereocenterCount;
	}
	/**
	 * @param definedAtomStereocenterCount the definedAtomStereocenterCount to set
	 */
	public void setDefinedAtomStereocenterCount(int definedAtomStereocenterCount) {
		this.definedAtomStereocenterCount = definedAtomStereocenterCount;
	}
	/**
	 * @return the undefinedAtomStereacenterCount
	 */
	public int getUndefinedAtomStereacenterCount() {
		return undefinedAtomStereacenterCount;
	}
	/**
	 * @param undefinedAtomStereacenterCount the undefinedAtomStereacenterCount to set
	 */
	public void setUndefinedAtomStereacenterCount(int undefinedAtomStereacenterCount) {
		this.undefinedAtomStereacenterCount = undefinedAtomStereacenterCount;
	}
	/**
	 * @return the definedBondStereocenterCount
	 */
	public int getDefinedBondStereocenterCount() {
		return definedBondStereocenterCount;
	}
	/**
	 * @param definedBondStereocenterCount the definedBondStereocenterCount to set
	 */
	public void setDefinedBondStereocenterCount(int definedBondStereocenterCount) {
		this.definedBondStereocenterCount = definedBondStereocenterCount;
	}
	/**
	 * @return the undefinedBondStereocenterCount
	 */
	public int getUndefinedBondStereocenterCount() {
		return undefinedBondStereocenterCount;
	}
	/**
	 * @param undefinedBondStereocenterCount the undefinedBondStereocenterCount to set
	 */
	public void setUndefinedBondStereocenterCount(int undefinedBondStereocenterCount) {
		this.undefinedBondStereocenterCount = undefinedBondStereocenterCount;
	}
	/**
	 * @return the covalentlyBondedUnitCount
	 */
	public int getCovalentlyBondedUnitCount() {
		return covalentlyBondedUnitCount;
	}
	/**
	 * @param covalentlyBondedUnitCount the covalentlyBondedUnitCount to set
	 */
	public void setCovalentlyBondedUnitCount(int covalentlyBondedUnitCount) {
		this.covalentlyBondedUnitCount = covalentlyBondedUnitCount;
	}
	/**
	 * @return the isCompoundCanonicalizable
	 */
	public boolean isCompoundCanonicalizable() {
		return isCompoundCanonicalizable;
	}
	/**
	 * @param isCompoundCanonicalizable the isCompoundCanonicalizable to set
	 */
	public void setCompoundCanonicalizable(boolean isCompoundCanonicalizable) {
		this.isCompoundCanonicalizable = isCompoundCanonicalizable;
	}
}
