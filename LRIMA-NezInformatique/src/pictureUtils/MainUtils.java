package pictureUtils;

import java.io.IOException;

/**
 * MAIN used to launch some of the Utils when needed to
 * @author m_bla
 *
 */
public class MainUtils {

	public static void main(String[] args) {
		try {
			FruitWriter.writeFruitsToCSV();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
