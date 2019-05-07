import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Collections;

public class HandDrawer {

	public static final String[] TRAIN_COLORS = { "Purple", "White", "Blue", "Yellow", "Orange", "Black", "Red",
			"Green", "Rainbow" };
	public static final Color[] COLOR_NAMES = { new Color(84, 22, 180), Color.WHITE, Color.BLUE, Color.YELLOW,
			Color.ORANGE, Color.BLACK, Color.RED, Color.GREEN, new Color(255, 0, 127) };
	public static final int ARR_SIZE = 10;
	public static ArrayList<Rectangle> clickableAdd = new ArrayList<Rectangle>();
	public static ArrayList<Rectangle> clickableSub = new ArrayList<Rectangle>();
	public static ArrayList<Rectangle> clickableArrow = new ArrayList<Rectangle>();
	public static ArrayList<Rectangle> clickableContracts = new ArrayList<Rectangle>();
	public static void init() {
			int x = 90;
			for ( int i = 0 ; i < 9; i++ ) { 
				clickableAdd.add(new Rectangle(x, 870, 50, 40));
				clickableSub.add(new Rectangle(x + 50, 870, 50, 40));
				x+= 140;
				
			}
			// adds the turn arrows for contract viewing 
			clickableArrow.add(new Rectangle(1050, 790, 35, 35));
			clickableArrow.add(new Rectangle(1315,790,35,35));
			
			clickableContracts.add(new Rectangle(600, 760, 320, 33));
			clickableContracts.add(new Rectangle(600, 793, 320, 33));
			clickableContracts.add(new Rectangle(600, 826, 320, 33));
			clickableContracts.add(new Rectangle(920, 760, 60, 100)); // this one is the "done" button
	}

	public static void drawHand(Graphics g, Player p) {
		init();
		Graphics2D g2 = (Graphics2D) g;
		//g2.setColor(new Color(255, 105, 180));
		
		g2.setColor(new Color(184, 134,11));
		g2.fillRect(0, 756, 1535, 325);
		g2.setColor(Color.BLACK);
		g2.drawRect(0, 755, 1535, 325);
		ArrayList<TrainCard> trainCard = p.getTrainCards();
		
		g2.setColor(Color.BLACK);
		int x = 90;
		
		for (int i = 0; i < COLOR_NAMES.length; i++) {
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(6));
			g2.drawRect(x, 870, 50, 210);
			g2.drawRect(x + 50, 870, 50, 210);
			
			g2.setStroke(new BasicStroke(1)); // replace these with images Cole made
			
			g2.setColor(COLOR_NAMES[i]);
			g2.fillRect(x+1, 910, 98, 170);
			
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(6));
			g2.drawLine(x, 910, x+100, 910);
			g2.setStroke(new BasicStroke(2));
			
			if (TRAIN_COLORS[i].equals("Black"))
				g2.setColor(Color.WHITE);
			else
				g2.setColor(Color.BLACK);
			
			g2.drawString(getCount(TRAIN_COLORS[i], trainCard), x + 28, 1010);
			x += 140;
		}
		
	}

	public static void drawContractSelection(Graphics g, ArrayList<ContractCard> selection) {
		init();
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(6));
		g2.drawRect(600, 760, 380, 100);
		g2.setStroke(new BasicStroke(1));
		g2.setColor(Color.WHITE);
		g2.fillRect(600, 760, 380, 100);
		g2.setColor(Color.BLACK);
		g2.drawRect(600, 760, 320, 33);
		g2.drawString(selection.get(0).getCity1().getName() + " to " + selection.get(0).getCity2().getName(), 610, 780);
		g2.drawRect(600, 793, 320, 33);
		g2.drawString(selection.get(1).getCity1().getName() + " to " + selection.get(1).getCity2().getName(), 610, 815);
		g2.drawRect(600, 826, 320, 33);
		g2.drawString(selection.get(2).getCity1().getName() + " to " + selection.get(2).getCity2().getName(), 610, 850);
	}
	
	public static void drawContractCards(Graphics g, ArrayList<ContractCard> cards) {
		init();
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(4));
		g2.setColor(Color.BLACK);
		g2.drawRect(1100, 765, 200, 85);
		g2.setColor(Color.WHITE);
		g2.fillRect(1100, 765, 199, 84);
		// draw contents here
		g2.setColor(Color.BLACK);
		
		//g2.drawString(cards.get(0).toString(), 1125, 800);
		g2.setColor(Color.BLACK);
		
		g2.drawRect(1050, 790, 35, 35);
		g2.drawRect(1315, 790, 35, 35);
		drawArrow(g, 1085, 807, 1050, 807 );
		drawArrow(g, 1315, 807, 1350, 807);		
		
		
	}
	
	public static void advanceCard(Player p, int direction) {
		init();
		if ( direction < 0 ) 
			Collections.rotate(p.getContracts(), 1);
		else if (direction > 0 )
			Collections.rotate(p.getContracts(), -1);
		//drawContractCards(g, p.getContracts());
	}

	private static String getCount(String string, ArrayList<TrainCard> list) {
		int count = 0;
		for (TrainCard c : list)
			if (c.getColor().equals(string))
				count++;
		return "" + count;
	}
	
	public static  void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
		init();
        Graphics2D g = (Graphics2D) g1.create();
        g.setStroke(new BasicStroke(3));
        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        // Draw horizontal arrow starting in (0, 0)
        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},
                      new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 3);
    }

	public static void drawPlayer(Graphics g, Player p) {
		init();
		Graphics2D g2 = (Graphics2D) g;

		
	}


}
