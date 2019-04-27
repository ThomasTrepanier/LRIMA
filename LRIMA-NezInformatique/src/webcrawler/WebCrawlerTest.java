package webcrawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import fileReading.Data1D;
import fileReading.Data2D;
import uk.ac.ebi.beam.Functions;
import uk.ac.ebi.beam.Graph;

public class WebCrawlerTest {

	//http://www.reciprocalnet.org/edumodules/commonmolecules/list.html
	//https://www.thoughtco.com/a-organic-compounds-list-608749
	static String folderPath = "Data/";
	final static String PUBCHEM_URL = "https://pubchem.ncbi.nlm.nih.gov/";
	final static String searchID = "search_1556195705352";
	public static void main(String[] args) throws IOException, InterruptedException, InvalidFormatException {
		String webPage = "https://www.google.ca/?hl=fr";
		
		//Load data from URL
		List<String> molecules = getElementsFromHTLMPage("http://www.reciprocalnet.org/edumodules/commonmolecules/list.html", "listFont", 100000);
		WebDriver driver = initWebDriver();
		driver.get(webPage);
		ArrayList<Data1D<String>> moleculesData = new ArrayList<Data1D<String>>();
		//First line for Identifiers
		ArrayList<String> firstLine = new ArrayList<String>();
		firstLine.add("Name");
		firstLine.add("SMILES");
		Data1D<String> firstLineData1D = new Data1D<String>(firstLine);
		moleculesData.add(firstLineData1D);
		firstLineData1D = null;
		firstLine = null;
		
		//Load all molecules from page
		for(int i = 0; i < molecules.size(); i++) {
			String s = molecules.get(i);
			ArrayList<String> line = new ArrayList<String>();
			line.add(s);
			line.add(getSmilesFromHomePage(driver, s));
			Data1D<String> dataLine = new Data1D<String>(line);
			moleculesData.add(dataLine);
		}
		//Put them all in 2D array
		Data2D<String> data = new Data2D<String>(moleculesData);
		data.writeInExcelFile(folderPath + "test_data.xlsx", "FullTest_1");
		data.print2DArray();
		driver.quit();
	}
	
	private static WebDriver initWebDriver() {
		String projectDir = System.getProperty("user.dir");
		System.setProperty("webdriver.chrome.driver", projectDir + "/lib/chromedriver/chromedriver.exe");
		return new ChromeDriver();
	}
	public static ArrayList<String> getElementsFromHTLMPage(String url, String id, int timeout) throws IOException{
		ArrayList<String> list = new ArrayList<String>();
		
		Document page = Jsoup.connect(url).timeout(timeout).get();
		List<Element> elements = page.getElementsByClass(id);
		for(Element e : elements) {
			list.add(e.text());
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
	
	public static String getSmilesFromHomePage(WebDriver driver, String identifier) throws InterruptedException {
		if(!driver.getCurrentUrl().equals(PUBCHEM_URL)) {
			goToMainPage(driver);
		}
		searchForIdentifier(driver, identifier);
		return getCanonicalSmilesFromPage(driver);
	}
	
	public static String getCanonicalSmilesFromPage(WebDriver driver) {
		WebElement smilesElement = null;
		try {
			smilesElement = getElementExplicitWait(driver, By.id("Canonical-SMILES"));
		} catch (NoSuchElementException e) {
			//e.printStackTrace();
			System.out.println("Couldn't find canonical Smiles in page " + driver.getCurrentUrl());
			return getIsomericSmilesFromPage(driver);
		}
		return findSmilesInElement(smilesElement);
	}
	
	public static String getIsomericSmilesFromPage(WebDriver driver) {
		WebElement smilesElement = null;
		try {
			smilesElement = getElementExplicitWait(driver, By.id("Isomeric-SMILES"));
		} catch (NoSuchElementException e) {
			System.out.println("Couldn't find isomeric Smiles in page " + driver.getCurrentUrl());
			return "";
		}
		return findSmilesInElement(smilesElement);
	}
	
	public static String findSmilesInElement(WebElement element) {
		String smilesText = element.getText();
		int firstSlashPos = smilesText.indexOf('\n');
		int lastSlashPos = smilesText.lastIndexOf('\n');
		String smiles = smilesText.substring(firstSlashPos + 1, lastSlashPos);
		return smiles;
	}
	
	public static void goToMainPage(WebDriver driver) {
		driver.navigate().to(PUBCHEM_URL);
	}
	
	public static void searchForIdentifier(WebDriver driver, String q) throws InterruptedException {
		WebElement searchBar = null;
		try {
			searchBar = getElementExplicitWait(driver, By.cssSelector("input"));
		} catch (NoSuchElementException e) {
			System.out.println("Couldn't find search bar in page " + driver.getCurrentUrl());
			return;
		}
		searchBar.sendKeys(q);
		searchBar.submit();
		clickOnBestIdentifier(driver);
	}
	public static void clickOnBestIdentifier(WebDriver driver) {
		WebElement result = null;
		try {
			List<WebElement> results = getElementsExplicitWait(driver, By.className("breakword"));
			if(results.size() > 0)
				result = results.get(0);
		} catch (NoSuchElementException e) {
			System.out.println("Coudln't find best result");
		}
		result.click();
	}
	
	public static WebElement getElementExplicitWait(WebDriver driver, By by) {
		return (new WebDriverWait(driver, 10)).
				until(ExpectedConditions.presenceOfElementLocated(by));
	}
	public static List<WebElement> getElementsExplicitWait(WebDriver driver, By by){
		return (new WebDriverWait(driver, 10)).
				until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
	}
}
