package MolecularProperties;

public class MolecularProperties_Chemical {
	private float xLogP3AA;
	private float topologicalPolarSurfaceArea;
	private int heavyAtomCount;
	private int formalCharge;
	private int complexity;
	private int isotopeAtomCount;
	private boolean isCompoundCanonicalizable;
	private MolecularProperties_BondCount bondCount;
	
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
	/**
	 * @return the bondCount
	 */
	public MolecularProperties_BondCount getBondCount() {
		return bondCount;
	}
	/**
	 * @param bondCount the bondCount to set
	 */
	public void setBondCount(MolecularProperties_BondCount bondCount) {
		this.bondCount = bondCount;
	}
}
