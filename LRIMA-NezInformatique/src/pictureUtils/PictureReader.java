package pictureUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;

import neural_network.NeuralNetwork;
import neural_network.StatUtil;
import neural_network.TrainingData;

public class PictureReader {

	static final String trainingFolder = "Data\\Fruit_Training\\";
	static final String testFolder = "Data\\Fruit_Test\\";
	static final int nbOfFruits = 10;

	public static void loadFruits(float percent) throws IOException {
		String picture = "Apple Braeburn/0_100.jpg";
		TrainingData[] tData = loadPicturesData(new File(trainingFolder), trainingFolder, percent);
		NeuralNetwork.tDataSet = tData;
		TrainingData[] testData = loadPicturesData(new File(testFolder), testFolder, percent);
		NeuralNetwork.testSet = testData;
	}

	private static TrainingData[] initializeTrainingData() {
		int nbOfPicutres = 0;
		File folderPathFile = new File(trainingFolder);
		nbOfPicutres = StatUtil.getNbOfFiles(folderPathFile);
		return new TrainingData[nbOfPicutres];
	}

	private static TrainingData[] loadPicturesData(File file, String mainFolder, float percent) throws IOException {
		int indexAt = 0;
		TrainingData[] datas = new TrainingData[StatUtil.getNbOfFiles(file)];
		for (int j = 0; j < file.listFiles().length * percent; j++) {
			File f = file.listFiles()[j];
			if (f.isDirectory()) {
				TrainingData[] subData = loadPicturesData(f, mainFolder, percent);
				int i = 0;
				for (i = 0; i < subData.length * percent; i++) {
					datas[indexAt + i] = subData[i];
				}
				indexAt += i;
			} else {
				String picturePath = f.getPath();
//				System.out.println(picturePath);
				String picture = picturePath.substring(picturePath.indexOf(mainFolder) + mainFolder.length());
				datas[indexAt] = getPictureData(f, picture);
				indexAt++;
			}
		}
		return datas;
	}

	private static TrainingData getPictureData(File f, String picture) throws IOException {
		BufferedImage image = ImageIO.read(f);
		int[] apexes = cropPicture(image);
		int[][] pixelsData = getPixels(image, apexes);
		float[] pictureData = convertToData(pixelsData);
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
				if (argb[1] < 240 && argb[2] < 240 && argb[3] < 240) {
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
				if (argb[1] < 240 && argb[2] < 240 && argb[3] < 240) {
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
				if (argb[1] < 240 && argb[2] < 240 && argb[3] < 240) {
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
				if (argb[1] < 240 && argb[2] < 240 && argb[3] < 240) {
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
		int[][] data = new int[w * h][4];

		for (int x = apexes[0]; x < apexes[2]; x++) {
			for (int y = apexes[1]; y < apexes[3]; y++) {
				int pixel = image.getRGB(x, y);
				data[x * y] = getARGBValues(pixel);
			}
		}
		return data;
	}

	private static float[] convertToData(int[][] pixels) {
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

	private static TrainingData getTrainingData(float[] data, String pictureName) {
		int slashId = pictureName.indexOf("\\");
		String name = "";
		for (int i = 0; i < slashId; i++) {
			name += (pictureName.charAt(i) + "");
		}

		int expectedIndex = getIndexOfPictureName(name);
		if (expectedIndex != -1) {
			float[] expectedOutput = new float[nbOfFruits];
			expectedOutput[expectedIndex] = 1f;
			float[] normalizedData = normalizeData(data, 1020, 0.01f);
			return new TrainingData(normalizedData, expectedOutput);
		}
		return null;
	}

	private static int getIndexOfPictureName(String name) {
		switch (name) {
		case ("Apple Braeburn"):
			return 0;
		case ("Banana"):
			return 1;
		case ("Avocado"):
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
		float[] normalizedData = new float[data.length];
		for (int i = 0; i < data.length; i++) {
			normalizedData[i] = data[i] / maxValue * 0.99f + minValue;
		}
		return normalizedData;
	}
}
