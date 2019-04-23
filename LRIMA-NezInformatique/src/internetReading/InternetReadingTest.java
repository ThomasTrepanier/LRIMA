package internetReading;

import java.io.IOException;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

public class InternetReadingTest {

	public static void main(String[] args) throws IOException {
		String webPage = "https://pubchem.ncbi.nlm.nih.gov/compound/180";
        String html = Jsoup.connect(webPage).get().html();
        
        System.out.println(html);
	}
}
