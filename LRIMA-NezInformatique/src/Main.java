
public class Main {

	public static void main(String[] args) {
		new Resources();
		Molecule limonene = new Molecule("(-)-Limonene");
		System.out.println(limonene.getIdentifier("SMILES"));
	}

}
