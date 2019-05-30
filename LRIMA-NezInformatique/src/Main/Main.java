package Main;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		new Resources();
		Molecule limonene = new Molecule("(-)-Limonene");
		Molecule ethanal = new Molecule("Ethanal");
		Molecule acetone = new Molecule("Acetone");
		Molecule methyl = new Molecule("Methyl");
		Molecule ethylene = new Molecule("Ethylene");
		Molecule adamantane = new Molecule("Adamantane");
		System.out.println("Limonene SMILES: " + limonene.getIdentifier(Identifier.SMILES));
		System.out.println("Ethanal Formula: " + CondensedFormula.getCondensedFormula(ethanal.getIdentifier(Identifier.SEMI_DEV_FORMULA)));
		System.out.println("Acetone Category: " + CondensedFormula.getMoleculeCategory(acetone.getIdentifier(Identifier.CONDENSED_FORMULA)));
		System.out.println("Methyl sub-category: " + CondensedFormula.CategoryOxygen(methyl.getIdentifier(Identifier.CONDENSED_FORMULA)));
		System.out.println("Ethylene sub-category: " + CondensedFormula.CategoryHydrocarbon(ethylene.getIdentifier(Identifier.CONDENSED_FORMULA)));
		System.out.println("Adamantane sub-category: " + CondensedFormula.getSubCategory(adamantane.getIdentifier(Identifier.CONDENSED_FORMULA)));
	}

}
