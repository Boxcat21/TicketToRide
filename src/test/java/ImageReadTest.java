import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageReadTest {

	public static void main(String[] args) throws IOException {
		
		BufferedImage bf = null;
		try { 
		bf = ImageIO.read(new File("UpdatedUIBetter.png"));
		}
		catch(Exception e) {}
		

	}

}
