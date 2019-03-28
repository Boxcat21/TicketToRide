import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class CityCoordInputs extends JComponent
{
	public CityCoordInputs()
	{		
	}
	public void paintComponent(Graphics g)
	{
		BufferedImage img = null;
		try {
				img = ImageIO.read(new File("UpdatedUIBetter.png"));
			} catch (IOException e) {
			}
		if(img==null)
			System.out.println("its null lmao");
		g.drawImage(img,0,0,null);
	}
	

}

