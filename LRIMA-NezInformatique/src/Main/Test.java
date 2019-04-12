package Main;
public class Test {

	public static void main(String[] args) {
		
		String molecule = "CH3-CH2-CH2-CH2-CH2-CH2-CH2-CH3";
		//String molecule = "C10H16";
		
		System.out.println(CondensedFormula.getCondensedFormula(molecule));
		//molecule = CondensedFormula.condensedFormula(molecule);
		//System.out.println();
		
		String B = CondensedFormula.getMoleculeCategory(molecule);
		System.out.println(B);
		
		String C = CondensedFormula.CategoryHydrocarbon(molecule);
		System.out.println(C);
		
		/*
		String C = CondensedFormula.CategoryOxygen(molecule);
		System.out.println(C);
		*/
		
		// !!! difference aldehyde cetone
	}

}
