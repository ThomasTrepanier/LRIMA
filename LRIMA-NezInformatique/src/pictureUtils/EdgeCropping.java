package pictureUtils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Arrays;

public class EdgeCropping {
	//Bias toward center
	double principalColorBias = 2;
	double centerBias = 0.5;
	public static BufferedImage crop(BufferedImage img, double tolerance) {
		//Get center color
		int xCenter = img.getWidth() / 2;
		int yCenter = img.getHeight() / 2;
		int centerColor = img.getRGB(xCenter, yCenter);
		int[] argb = PictureReader.getARGBValues(centerColor);
		
		
		int redMin = argb[1] - (int) (argb[1] * tolerance);
		int redMax = argb[1] + (int) (argb[1] * tolerance);
		int greenMin = argb[2] - (int) (argb[2] * tolerance);
		int greenMax = argb[2] + (int) (argb[2] * tolerance);
		int blueMin = argb[3] - (int) (argb[3] * tolerance);
		int blueMax = argb[3] + (int) (argb[3] * tolerance);
		
		IntRange redRange = new IntRange(redMin, redMax);
		IntRange greenRange = new IntRange(greenMin, greenMax);
		IntRange blueRange = new IntRange(blueMin, blueMax);
		
		System.out.println(redRange);
		System.out.println(greenRange);
		System.out.println(blueRange);
		//Get all pixels
		int imgStartX = img.getMinX();
		int imgStartY = img.getMinY();
		int imgWidth = img.getWidth();
		int imgHeight = img.getHeight();
		
		System.out.println("X: " + imgStartX + " Y:" + imgStartY + " W:" + imgWidth + " H:" + imgHeight);
		
		int[][] pixels = new int[imgWidth * imgHeight][4];
		System.out.println(pixels.length);
		for(int i = 0; i < imgWidth; i++) {
			for(int j = 0; j < imgHeight; j++) {
				pixels[i * imgHeight + j] = PictureReader.getARGBValues(img.getRGB(i, j));
			}
		}
		
		//Crop set white
		boolean redInRange = false;
		boolean greenInRange = false;
		boolean blueInRange = false;
		
		for(int i = 0; i < pixels.length; i++) {
			redInRange = false;
			greenInRange = false;
			blueInRange = false;
			
			if(redRange.isInRange(pixels[i][1])) {
				redInRange = true;
			}
			if(greenRange.isInRange(pixels[i][2])) {
				greenInRange = true;
			}
			if(blueRange.isInRange(pixels[i][3])) {
				blueInRange = true;
			}
			if(!(redInRange && greenInRange && blueInRange)) {
				for(int j = 0; j < pixels[i].length; j++) {
					pixels[i][j] = 255;
				}
			} 
		}
		
		//Create new picture
		BufferedImage croppedImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i < imgWidth; i++) {
			for(int j = 0; j < imgHeight; j++) {
				croppedImg.setRGB(i, j, toTypeIntARGB(pixels[i * imgHeight + j]));
			}
		}
		return croppedImg;
	}
	
	private static int toTypeIntARGB(int[] values) {
		return (values[0] << 24) | (values[1] << 16) | (values[2] << 8) | values[3];
	}
}
