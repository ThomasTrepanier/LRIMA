
public class Main {

	public static void main(String[] args) {
		Resources res = new Resources();
		Molecule limonene = new Molecule("(-)-Limonene");
		System.out.println(limonene.getIdentifier("SMILES"));
	}

}
