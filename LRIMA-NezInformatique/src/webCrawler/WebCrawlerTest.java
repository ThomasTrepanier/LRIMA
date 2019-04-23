package webCrawler;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebCrawlerTest {

	//id = js-rendered-content
	public static void main(String[] args) throws IOException, InterruptedException {
		String webPage = "https://pubchem.ncbi.nlm.nih.gov/compound/180";
		String projectDir = System.getProperty("user.dir");
		System.setProperty("webdriver.chrome.driver", projectDir + "/lib/chromedriver/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		
		driver.get(webPage);
		Thread.sleep(1000);
		WebElement smiles = driver.findElement(By.id("Canonical-SMILES"));
		System.out.println(smiles.getText());
		List<WebElement> list = driver.findElements(By.className("section-content-item"));
		System.out.println(list.size());
		for(int i = 0; i < 20; i++) {
			WebElement we = list.get(i);
			System.out.println(we.getTagName());
		}
		Thread.sleep(5000);
		driver.quit();
	}
}
