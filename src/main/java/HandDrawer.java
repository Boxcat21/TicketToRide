import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class HandDrawer {
	public HandDrawer() {
		
	}
	public static void drawHand(Graphics g, Player p) {
		Graphics2D g2 = (Graphics2D) g;
		g2.drawRect(0,	 720, 1440, 360);
		g2.setColor(Color.ORANGE);
		g2.fillRect(0, 721, 1440, 360);
		ArrayList<TrainCard> trainCard = p.getTrainCards();
		ArrayList<ContractCard> contracts = p.getContracts();
		
	}
}
