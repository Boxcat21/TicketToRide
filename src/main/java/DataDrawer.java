import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class DataDrawer {
	public DataDrawer() {
	
	}
	
	public static void drawData(Graphics g, ArrayList<Player> data) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(3));
		g2.drawLine(1535, 360, 1920, 360);
	}

}
