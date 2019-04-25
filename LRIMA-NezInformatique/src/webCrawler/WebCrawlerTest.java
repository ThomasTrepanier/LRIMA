package webCrawler;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import uk.ac.ebi.beam.Functions;
import uk.ac.ebi.beam.Graph;

public class WebCrawlerTest {

	//http://www.reciprocalnet.org/edumodules/commonmolecules/list.html
	//https://www.thoughtco.com/a-organic-compounds-list-608749
	final static String mainURL = "https://pubchem.ncbi.nlm.nih.gov/";
	final static String searchID = "search_1556195705352";
	public static void main(String[] args) throws IOException, InterruptedException {
		String webPage = "https://www.google.ca/?hl=fr";
		
		String projectDir = System.getProperty("user.dir");
		System.setProperty("webdriver.chrome.driver", projectDir + "/lib/chromedriver/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		
		driver.get(webPage);
		String smiles = getSmilesFromHomePage(driver, "Adamantane");
		Graph g = Graph.fromSmiles(smiles);
		Graph f = Functions.expand(g);
		System.out.println("Smiles: " + smiles + " Formula: " + f.toSmiles());
		//System.out.println(getSmilesFromHomePage(driver, "Acetone"));
		Thread.sleep(5000);
		driver.quit();
	}
	
	public static String getSmilesFromHomePage(WebDriver driver, String identifier) throws InterruptedException {
		if(!driver.getCurrentUrl().equals(mainURL)) {
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
		driver.navigate().to(mainURL);
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
