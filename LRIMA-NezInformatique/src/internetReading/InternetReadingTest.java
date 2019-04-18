package internetReading;

import java.io.IOException;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import uk.ac.ebi.beam.Functions;
import uk.ac.ebi.beam.Graph;

public class InternetReadingTest {

	//http://www.foo.com/
	//https://pubchem.ncbi.nlm.nih.gov/compound/180
	public static void main(String[] args) throws IOException {
		Graph g = Graph.fromSmiles("CH3-CO-CH3");
		Graph f = Functions.collapse(g);
		System.out.println(f.toSmiles());
		String webPage = "http://www.foo.com/";
        
        String html = Jsoup.connect(webPage).get().html();
        
        System.out.println(html);
        
//        String htmlBody = "<div id=\"js-rendered-content\"></div> ";
//        Document doc = Jsoup.parseBodyFragment(htmlBody);
//        Element body = doc.body();
//        System.out.println("\n" + body.text());
	}

}
