package webCrawler;

import java.io.IOException;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebCrawlerTest {

	//id = js-rendered-content
	public static void main(String[] args) throws IOException, InterruptedException {
		String webPage = "https://pubchem.ncbi.nlm.nih.gov/compound/180";
//        String html = Jsoup.connect(webPage).get().html();
//        
//        System.out.println(html);
		String projectDir = System.getProperty("user.dir");
		System.setProperty("webdriver.chrome.driver", projectDir + "/lib/chromedriver/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		
		driver.get(webPage);
		driver.findElements(By.id("js-rendered-content"));
		Thread.sleep(10000);
		driver.quit();
	}
}
