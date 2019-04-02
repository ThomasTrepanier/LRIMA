package MolecularProperties;

/**
 * ChemicoPhysical Properties of a molecule (important entry parameters)
 * @author m_bla
 *
 */
public class ChemicoPhysicalProperties {
	private MolecularProperties_Physical physProperties;
	private MolecularProperties_Chemical chemProperties;
	
	/**
	 * @return the physProperties
	 */
	public MolecularProperties_Physical getPhysProperties() {
		return physProperties;
	}

	/**
	 * @param physProperties the physProperties to set
	 */
	public void setPhysProperties(MolecularProperties_Physical physProperties) {
		this.physProperties = physProperties;
	}

	/**
	 * @return the chemProperties
	 */
	public MolecularProperties_Chemical getChemProperties() {
		return chemProperties;
	}

	/**
	 * @param chemProperties the chemProperties to set
	 */
	public void setChemProperties(MolecularProperties_Chemical chemProperties) {
		this.chemProperties = chemProperties;
	}
	
	public ChemicoPhysicalProperties(MolecularProperties_Physical physProps, MolecularProperties_Chemical chemProps) {
		this.setPhysProperties(physProps);
		this.setChemProperties(chemProps);
	}
}
