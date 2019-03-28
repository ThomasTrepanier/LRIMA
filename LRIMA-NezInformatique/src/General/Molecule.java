package General;

public class Molecule {
	/**
	 * Compound written in SMILES format
	 */
	private String composition;
	/**
	 * Molecule compound family (i.e. Hydrocarbure)
	 */
	private Molecule_Compound compound;
	
	public Molecule(String composition) {
		this.composition = composition;
	}
	public Molecule(String composition, Molecule_Compound compound) {
		this.composition = composition;
		this.compound = compound;
	} 
	
	public String getComposition() {
		return composition;
	}
	public void setComposition(String composition) {
		this.composition = composition;
	}
	public Molecule_Compound getCompound() {
		return compound;
	}
	public void setCompound(Molecule_Compound compound) {
		this.compound = compound;
	}
	
	public void findCompound() {
		
	}
}
