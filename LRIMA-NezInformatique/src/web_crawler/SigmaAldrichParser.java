package web_crawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import file_Reading.Data1D;
import file_Reading.Data2D;
import file_Reading.DataFill;
import file_Reading.ExcelSheetCompiler;
import file_Reading.FileReader;

public class SigmaAldrichParser {

	static String folderPath = "Data/";
	static String file = "test_data.xlsx";
	static String bu = "realtimeBackUp.txt";
	static String letterLinkPartial = "/etc/controller/controller-page";

	public static void main(String[] args) throws InvalidFormatException, IOException {
		File excelFile = new File(folderPath + file);
		int nbMol = 20;
		findData(nbMol);
		//Compile all sheets
		try {
			ExcelSheetCompiler.compileDataSheet(folderPath + file, "Sigma-Aldrich_Compiled2", 5, 5);
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Get SMILES
		Data2D<String> data = null;
		try {
			data = FileReader.readDataFile(new File(folderPath + file), "Sigma-Aldrich_Compiled2");
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		WebDriver driver = WebCrawlerTest.initWebDriver();
		driver.get("https://www.google.ca/?hl=fr");
		
		ArrayList<String> molecules = new ArrayList<String>();
		for(int i = 0; i < data.getDataSize(); i++) {
			Data1D<String> line = data.getLine(i);
			molecules.add(line.getValue1D(0));
		}
		
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
			ArrayList<String> line = null;
			try {
				line = WebCrawlerTest.getNameAndSmiles(driver, s);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(line != null) {
				Data1D<String> dataLine = new Data1D<String>(line);
				moleculesData.add(dataLine);
			}
		}
		//Put them all in 2D array
		Data2D<String> dataFilled = new Data2D<String>(moleculesData);
		try {
			dataFilled.writeInExcelFile(folderPath + file, "Sigma-Aldrich_Smiles");
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Fill Info
		try {
			Data2D<String> filledSA = FileReader.readDataFile(excelFile, "Sigma-Aldrich_Smiles");
			DataFill.fillMissingMoleculeData(filledSA);
			filledSA.writeInExcelFile(folderPath + file, "Sigma-Aldrich_Filled");
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Data2D<String> odorsList = FileReader.readDataFile(excelFile, "Sigma-Aldrich Compiled");
		Data2D<String> filled = FileReader.readDataFile(excelFile, "Sigma-Aldrich_Filled");
		for(Data1D<String> odorLine : odorsList.getData()) {
			String name = odorLine.getValue1D(0);
			String odor = odorLine.getValue1D(1);
			for(Data1D<String> filledLine : filled.getData()) {
				if(filledLine.getValue1D(0).equals(name)) {
					System.out.println(name + ", " + odor);
					filledLine.addValue(odor);
					break;
				}
			}
		}
		filled.writeInExcelFile(folderPath + file, "Sigma-Aldrich_Filled-Odors");
	}

	public static void findData(int nbMol) {
		int n = 0;
		String url = "https://www.sigmaaldrich.com/chemistry/chemistry-products.html?TablePage=15575210";

		FileWriter backupFile = null;
		PrintWriter writer = null;
		try {
			backupFile = new FileWriter(folderPath + bu);
			writer = new PrintWriter(backupFile);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ArrayList<Data1D<String>> sigmaFoundList = new ArrayList<Data1D<String>>();

		WebDriver driver = WebCrawlerTest.initWebDriver();
		driver.get(url);
		try {
			String[] identifiers = { "Name", "Odor(s)" };
			sigmaFoundList.add(new Data1D<String>(identifiers));

			for (n = 0; n < nbMol; n++) {
				removePopUp(driver);

				int idLetter = (int) (Math.random() * (8));
				boolean catChose = clickLinkFromLetter(driver, idLetter);
				removePopUp(driver);

				if (catChose) {
					List<WebElement> molecules = WebCrawlerTest.getElementsExplicitWait(driver, By.tagName("tr"), 8);
					removeAllSeparator(molecules);
					int idMolecule = (int) (Math.random() * molecules.size());
					WebElement molecule = molecules.get(idMolecule);
					List<WebElement> moleculeChildren = molecule.findElements(By.tagName("td"));
					if (moleculeChildren.size() > 0)
						moleculeChildren.get(0).click();

					removePopUp(driver);

					WebElement showHide = WebCrawlerTest.getElementExplicitWait(driver, By.className("showHideToggle"),
							8);
					if (showHide != null) {
						WebElement showHideButton = showHide.findElement(By.tagName("a"));
						if (showHideButton != null) {
							showHideButton.click();
							removePopUp(driver);
						} else {
							driver.navigate().to(url);
							n--;
							continue;
						}
					} else {
						driver.navigate().to(url);
						n--;
						continue;
					}

					WebElement nameElement = WebCrawlerTest.getElementExplicitWait(driver, By.tagName("h1"), 8);
					String name = nameElement.getText();

					List<WebElement> propertiesName = WebCrawlerTest.getElementsExplicitWait(driver,
							By.className("lft"), 8);
					int organolepticId = -1;
					for (int i = 0; i < propertiesName.size(); i++) {
						WebElement e = propertiesName.get(i);
						if (e.getText().contains("Organoleptic")) {
							organolepticId = i;
						}
					}
					if (organolepticId == -1) {
						driver.navigate().to(url);
						n--;
						continue;
					}

					List<WebElement> propertiesValue = WebCrawlerTest.getElementsExplicitWait(driver,
							By.className("rgt"), 8);
					String organoleptic = propertiesValue.get(organolepticId).getText();

					if (organoleptic.equals("") || organoleptic.equals("odorless")) {
						driver.navigate().to(url);
						n--;
						continue;
					}

					System.out.println(name + ": " + organoleptic);
					String[] info = { name, organoleptic };

					writer.println(info[0] + ": " + info[1]); // Backup in case crash

					Data1D<String> line = new Data1D<String>(info);
					sigmaFoundList.add(line);
					driver.navigate().to(url);
				}
			}
			writer.close();
			Data2D<String> data = new Data2D<String>(sigmaFoundList);
			data.writeInExcelFile(folderPath + file, "SigmaAldrich");
			driver.close();
		} catch (Exception e) {
			writer.close();

			Data2D<String> data = new Data2D<String>(sigmaFoundList);

			try {
				data.writeInExcelFile(folderPath + file, "SigmaAldrich");
			} catch (InvalidFormatException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			driver.close();
			findData(nbMol - n);
		}
	}

	public static boolean clickLinkFromLetter(WebDriver driver, int nb) throws Exception {
		WebElement link = null;
		System.out.println(nb);
		switch (nb) {
		case (0):
			link = WebCrawlerTest.getElementExplicitWait(driver, By.partialLinkText("A-B"), 8);
			break;
		case (1):
			link = WebCrawlerTest.getElementExplicitWait(driver, By.partialLinkText("C-D"), 8);
			break;
		case (2):
			link = WebCrawlerTest.getElementExplicitWait(driver, By.partialLinkText("E-F"), 8);
			break;
		case (3):
			link = WebCrawlerTest.getElementExplicitWait(driver, By.partialLinkText("G-H"), 8);
			break;
		case (4):
			link = WebCrawlerTest.getElementExplicitWait(driver, By.partialLinkText("I-L"), 8);
			break;
		case (5):
			link = WebCrawlerTest.getElementExplicitWait(driver, By.partialLinkText("M-N"), 8);
			break;
		case (6):
			link = WebCrawlerTest.getElementExplicitWait(driver, By.partialLinkText("O-P"), 8);
			break;
		case (7):
			link = WebCrawlerTest.getElementExplicitWait(driver, By.partialLinkText("Q-Z"), 8);
			break;
		}
		System.out.println(link.getText());
		if (link != null) {
			link.click();
			return true;
		} else
			return false;
	}

	public static void removeAllSeparator(List<WebElement> list) throws Exception {
		List<WebElement> toRemove = new ArrayList<WebElement>();
		for (WebElement we : list) {
			if (we.getAttribute("class").equals("opcparow"))
				toRemove.add(we);
		}
		list.removeAll(toRemove);
	}

	public static void removePopUp(WebDriver driver) throws Exception {
		System.out.println("Removing pop-ups...");

		if (WebCrawlerTest.getElementExplicitWait(driver, By.id("fsrInvite"), 5) != null) {
			List<WebElement> closeInvites = WebCrawlerTest.getElementsExplicitWait(driver,
					By.className("fsrAbandonButton"), 5);
			if (closeInvites != null) {
				for (WebElement e : closeInvites) {
					e.click();
					if (WebCrawlerTest.getElementExplicitWait(driver, By.id("fsrInvite"), 2) == null)
						break;
				}
			}
		}
		if (WebCrawlerTest.getElementExplicitWait(driver, By.id("selectCountryHeader"), 5) != null) {
			WebCrawlerTest.getElementExplicitWait(driver, By.partialLinkText("Canada (English)"), 5).click();
			;
		}
	}
}
