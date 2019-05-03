package fileReading;

import java.io.IOException;
import java.util.ArrayList;

import Main.CondensedFormula;
import uk.ac.ebi.beam.Functions;
import uk.ac.ebi.beam.Graph;

/**
 * Class used to fill a {@link Data2D} of molecule information to complete
 * basic information
 * 
 * @author Thomas Trepanier
 *
 */
public class DataFill {
	
	static ArrayList<String> indexes;
	static String name = "Name";
	static String smiles = "SMILES";
	static String formula = "Formula";
	static String condensed_formula = "CondensedFormula";
	static String family = "Family";
	static String sub_fam = "Sub-Family";
	
	public static ArrayList<String> initIndexes() {
		indexes = new ArrayList<String>();
		indexes.add(name);
		indexes.add(smiles);
		indexes.add(formula);
		indexes.add(condensed_formula);
		indexes.add(family);
		indexes.add(sub_fam);
		return indexes;
	}
	
	public static void verifyIndexRowIntegrity(Data2D<String> data) {
		Data1D<String> indexLine = data.getLine(0);
		for(String index : indexes) {
			if(!indexLine.getData1D().contains(index)) {
				indexLine.addValue(index);
			}
		}
	}
	
	/**
	 * Main class to call to fill the data
	 * @param data - Dataset to fill
	 * @throws IOException
	 */
	public static void fillMissingMoleculeData(Data2D<String> data) throws IOException {
		initIndexes();
		verifyIndexRowIntegrity(data);
		
		ArrayList<Data1D<String>> list2D = data.getData();
		
		for(int l = 0; l < list2D.size(); l++) {
			Data1D<String> list = list2D.get(l);
			if(list != null) {
				for(int i = 0; i < indexes.size(); i++) {
					String value = "";
					if(i < list.getDataSize())
						value = list.getValue1D(i);
					if(value.equals("")) {
						if(i == 0) { //Throw error, because can't not have the name
							System.out.println("Name not present at " + l + "," + i);
						} else {
							String keyType = getKeyType(data, i);
							String newValue = fillCell(data, list, keyType);
							
							if(i < list.getDataSize())
								list.removeValue(i);
							
							list.addValueAtRange(i, newValue);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Returns the String representing the name of the column
	 * @param data - Dataset to search
	 * @param i - Column index
	 * @return The name of the column
	 */
	public static String getKeyType(Data2D<String> data, int i) {
		return data.getValue2D(0, i);
	}
	
	/**
	 * Fills a specified cell in the data array
	 * @param data - Data cell is in
	 * @param line - Line where we need to feel
	 * @param keyType - Name of the column that need to be filled
	 * @return String to fill in the cell
	 * @throws IOException
	 */
	public static String fillCell(Data2D<String> data, Data1D<String> line, String keyType) throws IOException {
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
	
	/**
	 * Generates the SMILES for the line with the information available
	 * @param data - Data array to fill
	 * @param line - Line containing information to fill
	 * @return Value to fill in the cell
	 * @throws IOException
	 */
	public static String createSmiles(Data2D<String> data, Data1D<String> line) throws IOException { //TODO other way to load SMILES
		int formulaIndex = data.getColIndex(formula);
		String formula = line.getValue1D(formulaIndex);
		
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
	
	/**
	 * Generates the Formula for the line with the information available
	 * @param data - Data array to fill
	 * @param line - Line containing information to fill
	 * @return Value to fill in the cell
	 * @throws IOException
	 */
	public static String createFormula(Data2D<String> data, Data1D<String> line) throws IOException {
		int smilesIndex = data.getColIndex(smiles);
		String smiles = line.getValue1D(smilesIndex);
		
		Graph smilesGraph = null;
		try {
			smilesGraph = Graph.fromSmiles(smiles);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return "";
		}
		Graph formulaGraph = null;
		try {
			formulaGraph = Functions.expand(smilesGraph);
		} catch (Exception e) {
			//e.printStackTrace();
			return "";
		}
		
		return formulaGraph.toSmiles();
	}
	
	/**
	 * Generates the Condensed Formula for the line with the information available
	 * @param data - Data array to fill
	 * @param line - Line containing information to fill
	 * @return Value to fill in the cell
	 * @throws IOException
	 */
	public static String createCondensedFormula(Data2D<String> data, Data1D<String> line) {
		int formulaIndex = data.getColIndex(formula);
		String formula = line.getValue1D(formulaIndex);
		
		return CondensedFormula.getCondensedFormula(formula);
	}
	
	/**
	 * Generates the Family for the line with the information available
	 * @param data - Data array to fill
	 * @param line - Line containing information to fill
	 * @return Value to fill in the cell
	 * @throws IOException
	 */
	public static String createFamily(Data2D<String> data, Data1D<String> line) {
		int condensedFormulaIndex = data.getColIndex(condensed_formula);
		String condensedFormula = line.getValue1D(condensedFormulaIndex);
		
		return CondensedFormula.getMoleculeCategory(condensedFormula);
	}
	
	/**
	 * Generates the Sub-Family for the line with the information available
	 * @param data - Data array to fill
	 * @param line - Line containing information to fill
	 * @return Value to fill in the cell
	 * @throws IOException
	 */
	public static String createSubFamily(Data2D<String> data, Data1D<String> line) {
		int condensedFormulaIndex = data.getColIndex(condensed_formula);
		String condensedFormula = line.getValue1D(condensedFormulaIndex);
		
		return CondensedFormula.getSubCategory(condensedFormula);
	}
}
