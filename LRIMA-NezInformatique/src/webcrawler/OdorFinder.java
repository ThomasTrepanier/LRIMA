package webcrawler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import fileReading.Data1D;
import fileReading.Data2D;
import fileReading.FileReader;

public class OdorFinder {

	static String folderPath = "Data/";
	static String file = "test_data.xlsx";
	static String inputID = "common_name";
	static String buttonName = "btn-primary";
	public static void main(String[] args) throws IOException {
		String url = "https://cosylab.iiitd.edu.in/flavordb/search";
//		Document page = Jsoup.connect(url).timeout(10000).get();
//		String html = page.html();
//		Element nameInput = page.getElementById(inputID);
//		nameInput.text("Acetone");
//		System.out.println(nameInput);
		Data2D<String> data = null;
		try {
			data = FileReader.readDataFile(new File(folderPath + file), "Main Filled");
		} catch (InvalidFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		WebDriver driver = WebCrawlerTest.initWebDriver();
		driver.get(url);
		
		data.getLine(0).addValue("Odor");
		
		for(int i = 1; i < data.getDataSize(); i++) {
			Data1D<String> line = data.getLine(i);
			
			WebElement inputName = WebCrawlerTest.getElementExplicitWait(driver, By.id(inputID));
			String name = line.getValue1D(0);
			writeInWebElement(inputName, name);
			
			WebElement submitButton = WebCrawlerTest.getElementExplicitWait(driver, By.className(buttonName));
			submitButton.click();
			
			List<WebElement> oddRes = WebCrawlerTest.getElementsExplicitWait(driver, By.className("odd"));
			if(oddRes.get(0).getText().contains("No data available in table")) {
				driver.get(url);
				continue;
			}
			
			List<WebElement> evenRes = WebCrawlerTest.getElementsExplicitWait(driver, By.className("even"));
			ArrayList<WebElement> results = new ArrayList<WebElement>();
			if(oddRes != null)
				results.addAll(oddRes);
			if(evenRes != null)
				results.addAll(evenRes);
			
			for(WebElement e : results) {
				String[] info = getInfoInResult(e);
				if(info[0].equalsIgnoreCase(name)) {
					line.addValue(info[1]);
					System.out.println(info[0] + ": " + info[1]);
				}
			}
			driver.get(url);
		}
		
		try {
			data.writeInExcelFile(folderPath + file, "Main Odors");
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver.close();
	}
	
	public static void writeInWebElement(WebElement el, String s) {
		el.sendKeys(s);
	}
	
	public static String[] getInfoInResult(WebElement e) {
		String[] info = new String[2];
		String text = e.getText();
		String name = text.substring(0, text.indexOf(" "));
		String afterNameText = text.substring(text.indexOf(" ") + 1);
		String odors = afterNameText.substring(afterNameText.indexOf(" ") + 1, afterNameText.indexOf("More"));
		info[0] = name;
		info[1] = odors;
		return info;
	}
}
