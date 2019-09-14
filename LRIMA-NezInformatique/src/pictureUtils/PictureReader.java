package pictureUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import neural_network.NeuralNetwork;
import neural_network.StatUtil;
import neural_network.TrainingData;

//TODO: Load in batch
public class PictureReader {

	static final String TRAINING_FOLDER = "Data\\Fruits\\Fruit_Training\\";
	static final String TEST_FOLDER = "Data\\Fruits\\Fruit_Test\\";
	static final int NB_OF_FRUITS = 10;
	static final int PIXEL_CONSTANT = 10200000;
	static final int WHITE_CONSTANT = 240;

	public static void loadFruits(float percent, boolean isSum) throws IOException {
		String picture = "Apple Braeburn/0_100.jpg";
		ArrayList<TrainingData> tData = loadPicturesData(new File(TRAINING_FOLDER), TRAINING_FOLDER, percent, isSum);
		NeuralNetwork.tDataSet = tData;
		ArrayList<TrainingData> testData = loadPicturesData(new File(TEST_FOLDER), TEST_FOLDER, percent, isSum);
		NeuralNetwork.testSet = testData;
	}
	
	public static ArrayList<TrainingData>[] loadFruitsDL4J(float percent, boolean isSum) throws IOException {
		String picture = "Apple Braeburn/0_100.jpg";
		ArrayList<TrainingData> tData = loadPicturesData(new File(TRAINING_FOLDER), TRAINING_FOLDER, percent, isSum);
		ArrayList<TrainingData> testData = loadPicturesData(new File(TEST_FOLDER), TEST_FOLDER, percent, isSum);
		
		ArrayList<TrainingData>[] datas = new ArrayList[2];
		datas[0] = tData;
		datas[1] = testData;
		return datas;
	}
	
	public static ArrayList<TrainingData> loadFruitsFromFolder(String folder, boolean isSum) throws IOException {
		File fruitFile = new File(folder);
		//System.out.println(fruitFile);
		ArrayList<TrainingData> data = loadPicturesData(fruitFile, folder, 1f, isSum);
		return data;
	}

	private static TrainingData[] initializeTrainingData() {
		int nbOfPicutres = 0;
		File folderPathFile = new File(TRAINING_FOLDER);
		nbOfPicutres = StatUtil.getNbOfFiles(folderPathFile);
		return new TrainingData[nbOfPicutres];
	}

	private static ArrayList<TrainingData> loadPicturesData(File file, String mainFolder, float percent, boolean isSum) throws IOException {
		int indexAt = 0;
		ArrayList<TrainingData> datas = new ArrayList<TrainingData>();
		
		String fileName = file.toString() + "\\";
		System.out.println(fileName);
		
		//Determines how many files to load, if in the main, only load fruit number amount
		int filesToIterate = 0;
		if(fileName.toString().equals(TRAINING_FOLDER) || fileName.toString().equals(TEST_FOLDER)) {
			filesToIterate = NB_OF_FRUITS;
			System.out.println(file.listFiles().length);
		} else
			filesToIterate = file.listFiles().length;
		
		for (int j = 0; j < filesToIterate; j++) {
			File f = file.listFiles()[j];
//			System.out.println(f);
			if (f.isDirectory()) {
				ArrayList<TrainingData> subData = loadPicturesData(f, mainFolder, percent, isSum);
				int i = 0;
				for (i = 0; i < subData.size() * percent; i++) {
					if(datas.size() - 1< indexAt + i) {
						datas.add(subData.get(i));
					} else
						datas.set(indexAt + i, subData.get(i));
				}
				indexAt += i;
			} else {
				String picturePath = f.getPath();
				//System.out.println(picturePath);
				
				String picture = getPictureName(file, picturePath, mainFolder);
				//System.out.println(picture);
				if(datas.size() - 1 < indexAt) {
					datas.add(getPictureData(f, picture, isSum));
				} else
					datas.set(indexAt, getPictureData(f, picture, isSum));
				//System.out.println(datas[indexAt]);
				indexAt++;
			}
		}
		return datas;
	}

	private static TrainingData getPictureData(File f, String picture, boolean isSum) throws IOException {
		BufferedImage image = ImageIO.read(f);
		int[] apexes = cropPicture(image);
		int[][] pixelsData = getPixels(image, apexes);
		float[] pictureData = null;
		if(isSum)
			pictureData = convertToDataSum(pixelsData);
		else
			pictureData = convertToDataInd(pixelsData);
		TrainingData pictureTData = getTrainingData(pictureData, picture);
		return pictureTData;
	}

	private static int[] getARGBValues(int pixel) {
		int[] argb = new int[4];
		argb[0] = (pixel >> 24) & 0xff;
		argb[1] = (pixel >> 16) & 0xff;
		argb[2] = (pixel >> 8) & 0xff;
		argb[3] = (pixel) & 0xff;
		return argb;
	}

	private static int[] cropPicture(BufferedImage image) {
		int[] croppedApex = { -1, -1, -1, -1 };
		int w = image.getWidth();
		int h = image.getHeight();
//		System.out.println("Image size is " + w + " pixels by " + h);

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				int pixel = image.getRGB(x, y);
				int[] argb = getARGBValues(pixel);
				if (!isPixelWhite(argb)) {
					croppedApex[0] = x;
					break;
				}
			}
			if (croppedApex[0] != -1)
				break;
		}

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int pixel = image.getRGB(x, y);
				int[] argb = getARGBValues(pixel);
				if (!isPixelWhite(argb)) {
					croppedApex[1] = y;
					break;
				}
			}
			if (croppedApex[1] != -1)
				break;
		}

		for (int x = w - 1; x >= 0; x--) {
			for (int y = h - 1; y >= 0; y--) {
				int pixel = image.getRGB(x, y);
				int[] argb = getARGBValues(pixel);
				if (!isPixelWhite(argb)) {
					croppedApex[2] = x;
					break;
				}
			}
			if (croppedApex[2] != -1)
				break;
		}

		for (int y = h - 1; y >= 0; y--) {
			for (int x = w - 1; x >= 0; x--) {
				int pixel = image.getRGB(x, y);
				int[] argb = getARGBValues(pixel);
				if (!isPixelWhite(argb)) {
					croppedApex[3] = y;
					break;
				}
			}
			if (croppedApex[3] != -1)
				break;
		}

//		System.out.println("Color starts at " + croppedApex[0] + " in X and " + croppedApex[1] + " in Y");
//		System.out.println("Color ends at " + croppedApex[2] + " in X and " + croppedApex[3] + " in Y");
		return croppedApex;
	}

	private static int[][] getPixels(BufferedImage image, int[] apexes) {
		int w = image.getWidth();
		int h = image.getHeight();
		int[][] data = new int[w * h][3];
		
		//NO APEX
		apexes[0] = 0;
		apexes[2] = w;
		apexes[1] = 0;
		apexes[3] = h;
		
		for (int x = apexes[0]; x < apexes[2]; x++) {
			for (int y = apexes[1]; y < apexes[3]; y++) {
				int pixel = image.getRGB(x, y);
				int[] argbValue = getARGBValues(pixel);
				int[] rgbValue = {argbValue[1], argbValue[2], argbValue[3]};
				if(isPixelWhite(argbValue))
					rgbValue = new int[3];
				data[y + x *(apexes[3] - apexes[1])] = rgbValue;
				//System.out.println("X/Y: " + x + "/" + y + " ArrayPos: " + (y + x *(apexes[3] - apexes[1])) + " Value: " + Arrays.toString(argbValue));
			}
		}
		return data;
	}
	
	private static boolean isPixelWhite(int[] argb) {
		return argb[1] > WHITE_CONSTANT && argb[2] > WHITE_CONSTANT && argb[3] > WHITE_CONSTANT;
	}
	
	private static float[] convertToDataSum(int[][] pixels) {
		float[] data = new float[pixels.length];

		for (int i = 0; i < pixels.length; i++) {
			double sum = 0;
			for (int j = 0; j < pixels[0].length; j++) {
				sum += pixels[i][j];
//				data[i * pixels[0].length + j] = pixels[i][j];
			}
			data[i] = (float) (sum);
		}
		return data;
	}
	
	private static float[] convertToDataInd(int[][] pixels) {
		float[] data = new float[pixels.length * pixels[0].length];

		for (int i = 0; i < pixels.length; i++) {
			for (int j = 0; j < pixels[0].length; j++) {
				data[i * pixels[0].length + j]= pixels[i][j];
			}
		}
		return data;
	}
	
	private static TrainingData getTrainingData(float[] data, String pictureName) {
		int slashId = pictureName.indexOf("\\");
		String name = "";
		if(slashId < 0)
			name = pictureName;
		else {
			for (int i = 0; i < slashId; i++) {
				name += (pictureName.charAt(i) + "");
			}
		}
		
		int expectedIndex = getIndexOfPictureName(name);
		if (expectedIndex != -1) {
			float[] expectedOutput = new float[NB_OF_FRUITS];
			expectedOutput[expectedIndex] = 1f;
			float[] normalizedData = normalizeData(data, PIXEL_CONSTANT / data.length, 0.01f);
			return new TrainingData(normalizedData, expectedOutput);
		}
		return null;
	}
	
	private static String getPictureName(File file, String picturePath, String mainFolder) {
		if(mainFolder.equals(TRAINING_FOLDER) || mainFolder.equals(TEST_FOLDER)) {
			return picturePath.substring(picturePath.indexOf(mainFolder) + mainFolder.length());
		} else {
			return file.getName();
		}
		
	}

	private static int getIndexOfPictureName(String name) {
		switch (name) {
		case ("Apple Braeburn"):
			return 0;
		case ("Avocado"):
			return 1;
		case ("Banana"):
			return 2;
		case ("Cherry 1"):
			return 3;
		case ("Chestnut"):
			return 4;
		case ("Dates"):
			return 5;
		case ("Limes"):
			return 6;
		case ("Pear"):
			return 7;
		case ("Quince"):
			return 8;
		case ("Tomato 1"):
			return 9;
		default:
			return -1;
		}
	}

	private static float[] normalizeData(float[] data, float maxValue, float minValue) {
		//System.out.println(data.length);
		float[] normalizedData = new float[data.length];
		for (int i = 0; i < data.length; i++) {
			normalizedData[i] = data[i] / maxValue * 0.99f + minValue;
		}
		return normalizedData;
	}
}
