package MolecularProperties;

public class MolecularProperties_BondCount {
	private int hydrogenBondDonorCount;
	private int hydrogenBondAcceptorCount;
	private int rotatableBondCount;
	private int covalentlyBondedUnitCount;
	private MolecularProperties_StereoCenterCount stereoCenterCount;
	
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
	 * @return the stereoCenterCount
	 */
	public MolecularProperties_StereoCenterCount getStereoCenterCount() {
		return stereoCenterCount;
	}
	/**
	 * @param stereoCenterCount the stereoCenterCount to set
	 */
	public void setStereoCenterCount(MolecularProperties_StereoCenterCount stereoCenterCount) {
		this.stereoCenterCount = stereoCenterCount;
	}
}
