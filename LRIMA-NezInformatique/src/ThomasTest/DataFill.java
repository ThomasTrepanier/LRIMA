package ThomasTest;

import java.io.IOException;
import java.util.ArrayList;

import Main.CondensedFormula;
import uk.ac.ebi.beam.Functions;
import uk.ac.ebi.beam.Graph;

public class DataFill {
	
	static String name = "Name";
	static String smiles = "SMILES";
	static String formula = "Formula";
	static String condensed_formula = "CondensedFormula";
	static String family = "Family";
	static String sub_fam = "Sub-Family";
	
	public static void fillMissingMoleculeData(Data2D<String> data) throws IOException {
		ArrayList<ArrayList<String>> list2D = data.getData();
		
		for(int l = 0; l < list2D.size(); l++) {
			ArrayList<String> list = list2D.get(l);
			if(list != null) {
				for(int i = 0; i < list.size(); i++) {
					String value = list.get(i);
					if(value.equals("")) {
						if(i == 0) { //Throw error, because can't not have the name
							System.out.println("Name not present at " + l + "," + i);
						} else {
							String keyType = getKeyType(data, i);
							String newValue = fillCell(data, list, keyType);
							list.remove(i);
							list.add(i, newValue);
						}
					}
				}
			}
		}
	}
	
	public static String getKeyType(Data2D<String> data, int i) {
		return data.getValue2D(0, i);
	}
	
	public static String fillCell(Data2D<String> data, ArrayList<String> line, String keyType) throws IOException {
		String value = "";
		switch(keyType) {
		case ("SMILES"):
			value = createSmiles(data, line);
			break;
		case("Formula"):
			value = createFormula(data, line);
			break;
		case("CondensedFormula"):
			value = createCondensedFormula(data, line);
			break;
		case("Family"):
			value = createFamily(data, line);
			break;
		case("Sub-Family"):
			value = createSubFamily(data, line);
			break;
		}
		return value;
	}
	
	public static String createSmiles(Data2D<String> data, ArrayList<String> line) throws IOException { //TODO other way to load SMILES
		int formulaIndex = data.getColIndex(formula);
		String formula = line.get(formulaIndex);
		
		Graph formulaGraph = null;
		try {
			formulaGraph = Graph.fromSmiles(formula);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Graph smilesGraph = Functions.collapse(formulaGraph);
		
		return smilesGraph.toSmiles();
	}
	
	public static String createFormula(Data2D<String> data, ArrayList<String> line) throws IOException {
		int smilesIndex = data.getColIndex(smiles);
		String smiles = line.get(smilesIndex);
		
		Graph smilesGraph = null;
		try {
			smilesGraph = Graph.fromSmiles(smiles);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Graph formulaGraph = Functions.expand(smilesGraph);
		
		return formulaGraph.toSmiles();
	}
	
	public static String createCondensedFormula(Data2D<String> data, ArrayList<String> line) {
		int formulaIndex = data.getColIndex(formula);
		String formula = line.get(formulaIndex);
		
		return CondensedFormula.getCondensedFormula(formula);
	}
	
	public static String createFamily(Data2D<String> data, ArrayList<String> line) {
		int condensedFormulaIndex = data.getColIndex(condensed_formula);
		String condensedFormula = line.get(condensedFormulaIndex);
		
		return CondensedFormula.getMoleculeCategory(condensedFormula);
	}
	
	public static String createSubFamily(Data2D<String> data, ArrayList<String> line) {
		int condensedFormulaIndex = data.getColIndex(condensed_formula);
		String condensedFormula = line.get(condensedFormulaIndex);
		
		return CondensedFormula.getSubCategory(condensedFormula);
	}
}
