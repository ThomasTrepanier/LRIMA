package pictureUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * MAIN used to launch some of the Utils when needed to
 * @author m_bla
 *
 */
public class MainUtils {
	
	static final String picture = "Data\\Fruits\\apple.jpg";
	static final String outPath = "Data\\Fruits\\apple_cropped.jpg";
	public static void main(String[] args) {
		File file = new File(picture);
		try {
			long startTime = System.nanoTime();
			BufferedImage img = ImageIO.read(file);
			BufferedImage croppedImg = EdgeCropping.crop(img, 0.5);
			File outFile = new File(outPath);
			boolean successful = ImageIO.write(croppedImg, "png", outFile);
			System.out.println("Done " + successful + " " + (System.nanoTime() - startTime) / 1000000 + "ms");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
