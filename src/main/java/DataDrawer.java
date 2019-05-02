import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class DataDrawer {
	public DataDrawer() {
	
	}
	
	public static void drawData(Graphics g, ArrayList<Player> data) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(3));
		
		g2.drawLine(1535, 180, 1920, 180);
		g2.drawLine(1535, 360, 1920, 360);
		g2.drawLine(1535, 540, 1920, 540);
		g2.drawLine(1535, 720, 1920, 720);
		System.out.println(1);
	}
	
	public static void drawDisplayCards(Graphics g, ArrayList<TrainCard> display) {
		Graphics2D g2 = (Graphics2D) g;
		
		
	}

}
