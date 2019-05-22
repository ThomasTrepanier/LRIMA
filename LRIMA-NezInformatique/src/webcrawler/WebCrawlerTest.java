package webcrawler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fileReading.Data1D;
import fileReading.Data2D;
import fileReading.ExcelSheetCompiler;
import fileReading.FileReader;
import fileReading.Utils;
import uk.ac.ebi.beam.Functions;
import uk.ac.ebi.beam.Graph;

//TODO Gather all sheets into one with no duplicates, PUBCHEM read should get name from page not from list
public class WebCrawlerTest {

	//https://www.thoughtco.com/a-organic-compounds-list-608749
	//http://www.fullsense.com/Application/ChemicalOrganic/List_of_organic_compounds.htm
	//https://sciencestruck.com/organic-compounds-list
	//https://www.jagranjosh.com/general-knowledge/list-of-important-organic-compounds-1456306311-1
	static String folderPath = "Data/";
	static String file = "test_data.xlsx";
	final static String PUBCHEM_URL = "https://pubchem.ncbi.nlm.nih.gov/";
	final static String searchID = "search_1556195705352";
	public static void main(String[] args) throws IOException, InterruptedException, InvalidFormatException {
		String webPage = "https://www.google.ca/?hl=fr";
		
		String url = "http://www.fullsense.com/Application/ChemicalOrganic/List_of_organic_compounds.htm";
		String id = "bodyContent0";
		
		//Load data from URL
//		List<String> molecules = getElementsFromFullSense(url, id, 20000);
//		for(String s : molecules) {
//			System.out.println(s);
//		}
//		
//		WebDriver driver = initWebDriver();
//		driver.get(webPage);
		
		//TEST
//		driver.navigate().to("http://www.fullsense.com/Application/ChemicalOrganic/List_of_organic_compounds.htm");
//		WebElement mainResults = getElementExplicitWait(driver, By.id("bodyContent0"));
//		System.out.println(mainResults.getText());
//		System.exit(0);
		
//		ArrayList<Data1D<String>> moleculesData = new ArrayList<Data1D<String>>();
//		//First line for Identifiers
//		ArrayList<String> firstLine = new ArrayList<String>();
//		firstLine.add("Name");
//		firstLine.add("SMILES");
//		Data1D<String> firstLineData1D = new Data1D<String>(firstLine);
//		moleculesData.add(firstLineData1D);
//		firstLineData1D = null;
//		firstLine = null;
//		
//		//Load all molecules from page
//		for(int i = 0; i < molecules.size(); i++) {
//			String s = molecules.get(i);
//			ArrayList<String> line = getNameAndSmiles(driver, s);
//			if(line != null) {
//				Data1D<String> dataLine = new Data1D<String>(line);
//				moleculesData.add(dataLine);
//			}
//		}
//		//Put them all in 2D array
//		Data2D<String> data = new Data2D<String>(moleculesData);
//		data.writeInExcelFile(folderPath + file, "FullSense");
		
		//Puts them all in one sheet with no duplicates
		ExcelSheetCompiler.compileDataSheet(folderPath + file, "Main", 0, 2);
		//Loads them in Data2D array to fill
		Data2D<String> filledData = FileReader.readDataFile(new File(folderPath + file), "Main");
		filledData.writeInExcelFile(folderPath + file, "Main Filled");
		//driver.quit();
	}
	
	public static WebDriver initWebDriver() {
		String projectDir = System.getProperty("user.dir");
		System.setProperty("webdriver.chrome.driver", projectDir + "/lib/chromedriver/chromedriver74.exe");
		return new ChromeDriver();
	}
	
	//Modifier en fonction du site
	public static ArrayList<String> getElementsFromReciprocalNet(String url, String id, int timeout) throws IOException{
		ArrayList<String> list = new ArrayList<String>();
		
		Document page = Jsoup.connect(url).timeout(timeout).get();
		List<Element> elements = page.getElementsByClass(id);
		for(Element e : elements) {
			list.add(e.text());
		}
		return list;
	}
	
	public static ArrayList<String> getElementsFromThoughtCo(String url, String id, int timeout) throws IOException {
		Document page = Jsoup.connect(url).get();
		System.out.println(page.getElementById(id).text());
		String text = page.getElementById(id).text();
		ArrayList<String> list = new ArrayList<String>();
		int pos1 = 0;
		int pos2 = 0;
		String subText = "";
		int i = 0;
		while(i < text.length() - 2) {
			String name = "";
			char c = text.charAt(i);
			subText = text.substring(i);
			if(Character.isUpperCase(c)) {
				if(Utils.isNumeric(subText.charAt(1) + "") || Character.isUpperCase(subText.charAt(1)) || subText.charAt(1) == ' ' || Utils.isNumeric(subText.charAt(2) + "") || Character.isUpperCase(subText.charAt(2))) {
					i++;
					continue;
				}
				pos1 = subText.indexOf(" ");
				char k = subText.charAt(pos1 + 1);
				if(Character.isLetter(k)) {
					pos2 = subText.substring(pos1 + 1).indexOf(" ") + pos1 + 1;
					name = subText.substring(0, pos2);
					i += pos2 - 1;
				} else {
					name = subText.substring(0, pos1);
					i += pos1 - 1;
				}
			}
			if(!name.equals("")) {
				list.add(name);
			}
			i++;
		}
		return list;
	}
	
	public static ArrayList<String> getElementsFromFullSense(String url, String id, int timeout) throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		Document doc = Jsoup.connect(url).timeout(timeout).get();
		Element content = doc.getElementById(id);
		List<Node> nodes = content.childNodes();
		String nodeId = "title=";
		for(Node nodeLine : nodes) {
			for(Node node : nodeLine.childNodes()) {
				String nodeText = node.toString();
				int titlePos = nodeText.indexOf(nodeId);
				if(titlePos != -1) {
					titlePos += nodeId.length() + 1;
					int endPos = nodeText.substring(titlePos).indexOf("\"") + titlePos;
					String name = nodeText.substring(titlePos, endPos);
					//Handle "page does not exists"
					int i = 0;
					while(i < name.length() && name.substring(i).indexOf(" ") != -1) {
						int spacePos = name.substring(i).indexOf(" ") + i;
						if(spacePos != -1 && name.charAt(spacePos + 1) == '(') {
							name = name.substring(0, spacePos);
						}
						i += spacePos + 1;
					}
					if(name.indexOf("Edit") == -1)
						list.add(name);
				}
			}
		}
		return list;
	}
	public static ArrayList<String> getSmilesAndFormula(WebDriver driver, String identifier) throws InterruptedException, IOException {
		String smiles = getSmilesFromHomePage(driver, identifier);
		Graph g = Graph.fromSmiles(smiles);
		Graph f = Functions.expand(g);
		String formula = f.toSmiles();
		
		ArrayList<String> result = new ArrayList<String>();
		result.add(smiles);
		result.add(formula);
		return result;
	}
	
	public static ArrayList<String> getNameAndSmiles(WebDriver driver, String identifier) throws InterruptedException{
		ArrayList<String> list = new ArrayList<String>();
		String smiles = getSmilesFromHomePage(driver, identifier);
		
//		if(smiles.equals(""))
//			return null;
		
//		String name = getNameOnPage(driver);
//		if(name.equals("")) {
//			
//		} else {
//			list.add(name);
//		}
		
		list.add(identifier);
		list.add(smiles);
		return list;
	}
	
	public static String getNameOnPage(WebDriver driver) {
		WebElement nameElement = null;
		try {
			nameElement = getElementExplicitWait(driver, By.className("p-sm-top"), 10);
		} catch (NoSuchElementException e) {
			System.out.println("Couldn't find name on page");
			return "";
		}
		return nameElement.getText();
	}
	public static String getSmilesFromHomePage(WebDriver driver, String identifier) throws InterruptedException {
		if(!driver.getCurrentUrl().equals(PUBCHEM_URL)) {
			goToMainPage(driver);
		}
		boolean haveResults = searchForIdentifier(driver, identifier);
		if(!haveResults)
			return "";
		
		String smiles = getCanonicalSmilesFromPage(driver);
		return smiles;
	}
	
	public static String getCanonicalSmilesFromPage(WebDriver driver) {
		WebElement smilesElement = null;

		smilesElement = getElementExplicitWait(driver, By.id("Canonical-SMILES"), 10);
		
		 if(smilesElement == null) {
			//e.printStackTrace();
			System.out.println("Couldn't find canonical Smiles in page " + driver.getCurrentUrl());
			String isomericSmiles = getIsomericSmilesFromPage(driver);
			if(!isomericSmiles.equals(""))
				return isomericSmiles;
		}
		return findSmilesInElement(smilesElement);
	}
	
	public static String getIsomericSmilesFromPage(WebDriver driver) {
		WebElement smilesElement = null;
		smilesElement = getElementExplicitWait(driver, By.id("Isomeric-SMILES"), 10); 
		if(smilesElement == null){
			System.out.println("Couldn't find isomeric Smiles in page " + driver.getCurrentUrl());
			return "";
		}
		return findSmilesInElement(smilesElement);
	}
	
	public static String findSmilesInElement(WebElement element) {
		if(element == null)
			return "";
		
		String smilesText = element.getText();
		int firstSlashPos = smilesText.indexOf('\n');
		int lastSlashPos = smilesText.lastIndexOf('\n');
		String smiles = smilesText.substring(firstSlashPos + 1, lastSlashPos);
		return smiles;
	}
	
	public static void goToMainPage(WebDriver driver) {
		driver.navigate().to(PUBCHEM_URL);
	}
	
	public static boolean searchForIdentifier(WebDriver driver, String q) {
		WebElement searchBar = null;
		try {
			searchBar = getElementExplicitWait(driver, By.cssSelector("input"), 10);
		} catch (NoSuchElementException e) {
			System.out.println("Couldn't find search bar in page " + driver.getCurrentUrl());
			return false;
		}
		searchBar.sendKeys(q);
		searchBar.submit();
		return hasResults(driver);
	}
	
	//DOESNT WORK FOR NO REASION
	public static boolean hasResults(WebDriver driver) {
		WebElement mainResults = getElementExplicitWait(driver, By.id("main-results"), 10);
		//System.out.println(mainResults.getText());
		if(!mainResults.getText().equals("0 results found...")) {
			clickOnBestIdentifier(driver);
			return true;
		} else {
			System.out.println("No results");
			return false;
		}
	}
	public static void clickOnBestIdentifier(WebDriver driver) {
		WebElement result = null;
		try {
			List<WebElement> results = getElementsExplicitWait(driver, By.className("breakword"), 10);
			if(results.size() > 0)
				result = results.get(0);
			result.click();
		} catch (Exception e) {
			System.out.println("Coudln't find best result");
			return;
		}
	}
	
	public static WebElement getElementExplicitWait(WebDriver driver, By by, int timeout) {
		WebElement element = null;
		try {
			element = new WebDriverWait(driver, timeout).until(ExpectedConditions.presenceOfElementLocated(by));
		} catch (Exception e) {
			System.out.println("Waited and didn't find element " + by);
		}
		return element;
	}
	public static List<WebElement> getElementsExplicitWait(WebDriver driver, By by, int timeout){
		List<WebElement> elements = null;
		try {
			elements = new WebDriverWait(driver, timeout).
				until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
		} catch (Exception e) {
			System.out.println("Waited and didnt't find elements " + by);
		}
		return elements;
	}
}
