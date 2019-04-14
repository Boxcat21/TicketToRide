import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class HandDrawer {
	
	public static final String[] TRAIN_COLORS = { "Purple", "White", "Blue", "Yellow", "Orange", "Black", "Red",
			"Green", "Rainbow"};
	public static final Color[] COLOR_NAMES = { new Color(84,22,180), Color.WHITE, Color.BLUE, Color.YELLOW, Color.ORANGE, Color.BLACK, Color.RED, Color.GREEN, new Color(255,0,127)};
	
	public HandDrawer() {
		
	}
	public static void drawHand(Graphics g, Player p) {
		Graphics2D g2 = (Graphics2D) g;
		g2.drawRect(0,	 720, 1440, 360);
		g2.setColor(Color.LIGHT_GRAY);
		g2.fillRect(0, 721, 1440, 360);
		ArrayList<TrainCard> trainCard = p.getTrainCards();
		ArrayList<ContractCard> contracts = p.getContracts();
		g2.setColor(Color.BLACK);
		int x = 60;
		for ( int i = 0; i < COLOR_NAMES.length; i++) {
			g2.setColor(Color.BLACK);
			g2.drawRect(x, 900, 60, 180);
			g2.setColor(COLOR_NAMES[i]);
			g2.fillRect(x, 940, 60, 120);
			x+=120;
		}
		
		
		
	}
	
	
}
