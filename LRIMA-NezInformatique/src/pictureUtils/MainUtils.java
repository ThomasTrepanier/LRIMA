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
    //Canny parameters
    private static final double CANNY_THRESHOLD_RATIO = .2; //Suggested range .2 - .4
    private static final int CANNY_STD_DEV = 1;             //Range 1-3
    
	public static final String picture = "Data\\Fruits\\apple.jpg";
	static final String outPath = "Data\\Fruits\\apple_cropped_canny.jpg";
	public static void main(String[] args) {
		File file = new File(picture);
		try {
			long startTime = System.nanoTime();
			BufferedImage img = ImageIO.read(file);
			BufferedImage croppedImg = null;
//			croppedImg = EdgeCropping.crop(img, 0.5);
			croppedImg = jcanny.JCanny.CannyEdges(img, CANNY_STD_DEV, CANNY_THRESHOLD_RATIO);
			File outFile = new File(outPath);
			boolean successful = ImageIO.write(croppedImg, "png", outFile);
			System.out.println("Done " + successful + " " + (System.nanoTime() - startTime) / 1000000 + "ms");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
