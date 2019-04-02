package Main;
public class CondensedFormula {

	public static String condensedFormula(String halfCondensedFormula) {
		int nbC = 0, nbH = 0, nbO = 0;
		halfCondensedFormula.toUpperCase();
		halfCondensedFormula.replaceAll("-", "");

		for (int k = 0; k < halfCondensedFormula.length(); k++) {
			if (halfCondensedFormula.charAt(k) == 'C') {
				int n = (int) halfCondensedFormula.charAt(k + 1) - '0';
				if (n == 1 || n == 2 || n == 3 || n == 4 || n == 5 || n == 6 || n == 7 || n == 8 || n == 9) {
					k++;
					nbC += n;
				} else {
					nbC++;
				}
			}
			if (halfCondensedFormula.charAt(k) == 'H') {
				int n = (int) halfCondensedFormula.charAt(k + 1) - '0';
				if (n == 1 || n == 2 || n == 3 || n == 4 || n == 5 || n == 6 || n == 7 || n == 8 || n == 9) {
					k++;
					nbH += n;
				} else {
					nbH++;
				}
			}
			if (halfCondensedFormula.charAt(k) == 'O') {
				int n = (int) halfCondensedFormula.charAt(k + 1) - '0';
				if (n == 1 || n == 2 || n == 3 || n == 4 || n == 5 || n == 6 || n == 7 || n == 8 || n == 9) {
					k++;
					nbO += n;
				} else {
					nbO++;
				}
			}
		}

		if (nbO != 0) {
			halfCondensedFormula = "C" + nbC + "H" + nbH + "O" + nbO;
		} else {
			halfCondensedFormula = "C" + nbC + "H" + nbH;
		}

		return halfCondensedFormula;
	}
	
	public static String getCondensedFormula(String formula) {
		String[] atoms = {"C","H","O","N"};
		boolean[] containsAtom = new boolean[atoms.length];
		int[] atomCount = new int[atoms.length];
		
		//Find all atoms and their respective count
		for(int i = 0; i < formula.length(); i++) { //Iterates through the string
			for(int j = 0; j < atoms.length; j++) { //Iterates through the atom array
				if((formula.charAt(i) + "").equals(atoms[j])) { //If the char is an atom
					containsAtom[j] = true; //Sets the atom boolean to true (their is an atom of this sort)
					if(i < formula.length() - 1) //If its the last atom of the string, we can't count how many or it will cause an execption
						atomCount[j] += findCount(formula.substring(i+1)); //Counts how much their is in this part
					else
						atomCount[j]++; 
					break;
				}
			}
		}
		
		//Creates condensed formula
		String condensedFormula = "";
		for(int i = 0; i < atoms.length; i++) {
			if(containsAtom[i]) {
				condensedFormula += atoms[i] + atomCount[i];
			}
		}
		return condensedFormula;
	}
	
	private static int findCount(String s) {
		String count = "";
		int i = 0;
		while(i < s.length() && isNumeric(s.charAt(i) + "")) {
			count += s.charAt(i);
			i++;
		}
		if(count.equals("")) {
			return 1;
		} else
			return Integer.parseInt(count);
	}
	private static boolean isNumeric(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	public static String CategoryMolecule(String condensedFormula) {
		String category;
		condensedFormula.toUpperCase();

		if (condensedFormula.indexOf('O') == -1) {
			category = "hydrocarbon";

		} else if (condensedFormula.indexOf('O') != -1) {
			category = "oxygen";

		} else {
			category = "ERROR";
		}

		return category;
	}
	
	public static String getSubCategory(String condensedFormula) {
		String category = CategoryMolecule(condensedFormula);
		String subCategory = "";
		switch(category) {
		case ("hydrocarbon"):
			subCategory = CategoryHydrocarbon(condensedFormula);
			break;
		case ("oxygen"):
			subCategory = CategoryOxygen(condensedFormula);
			break;
		default:
			subCategory = "";
			break;
		}
		return subCategory;
	}
	public static String CategoryHydrocarbon(String condensedFormula) {
		if(!CategoryMolecule(condensedFormula).equals("hydrocarbon")) //Not an hydrocarbon
			return "";
		
		String category;
		condensedFormula.toUpperCase();
		condensedFormula += " ";
		
		double indiceC = 0;
		char C1 = condensedFormula.charAt(condensedFormula.indexOf('C') + 1);
		char m = condensedFormula.charAt(condensedFormula.indexOf('C') + 2);
		if (m == '0' || m == '1' || m == '2' || m == '3' || m == '4' || m == '5' || m == '6' || m == '7' || m == '8'
				|| m == '9') {
			char C2 = m;
			indiceC = 10 * (C1 - '0') + (C2 - '0');

		} else {
			indiceC = C1 - '0';
		}

		double indiceH = 0;
		char H1 = condensedFormula.charAt(condensedFormula.indexOf('H') + 1);
		char n = condensedFormula.charAt(condensedFormula.indexOf('H') + 2);
		if (n == '0' || n == '1' || n == '2' || n == '3' || n == '4' || n == '5' || n == '6' || n == '7' || n == '8'
				|| n == '9') {
			char H2 = n;
			indiceH = 10 * (H1 - '0') + (H2 - '0');

		} else {
			indiceH = H1 - '0';
		}

		if (indiceH / 2 == indiceC) {
			category = "alcene";

		} else if (indiceH / indiceC == 1.6) {
			category = "terpene";

		} else {
			category = "ERROR Autre";
		}

		return category;
	}

	public static String CategoryOxygen(String condensedFormula) {
		if(!CategoryMolecule(condensedFormula).equals("oxygen")) //Not an oxygene
			return "";
		
		String category;
		condensedFormula.toUpperCase();
		condensedFormula += " ";

		double indiceC = 0;
		char C1 = condensedFormula.charAt(condensedFormula.indexOf('C') + 1);
		char n = condensedFormula.charAt(condensedFormula.indexOf('C') + 2);
		if (n == '0' || n == '1' || n == '2' || n == '3' || n == '4' || n == '5' || n == '6' || n == '7' || n == '8'
				|| n == '9') {
			char C2 = n;
			indiceC = 10 * (C1 - '0') + (C2 - '0');

		} else {
			indiceC = C1 - '0';
		}

		double indiceH = 0;
		char H1 = condensedFormula.charAt(condensedFormula.indexOf('H') + 1);
		char m = condensedFormula.charAt(condensedFormula.indexOf('H') + 2);
		if (m == '0' || m == '1' || m == '2' || m == '3' || m == '4' || m == '5' || m == '6' || m == '7' || m == '8'
				|| m == '9') {
			char H2 = m;
			indiceH = 10 * (H1 - '0') + (H2 - '0');

		} else {
			indiceH = H1 - '0';
		}

		double indiceO = condensedFormula.charAt(condensedFormula.indexOf('O') + 1) - '0';

		if (indiceH / indiceC == 2 && indiceO == 1) {
			category = "aldehyde";

		} else if (indiceO == 1) {
			category = "cetone";

		} else if (indiceO == 2) {
			category = "ester";

		} else {
			category = "ERROR Autre";
		}

		return category;
	}

}
