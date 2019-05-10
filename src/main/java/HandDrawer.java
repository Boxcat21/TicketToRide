import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;

public class HandDrawer {

	public static final String[] TRAIN_COLORS = {"magenta","white","blue","yellow","orange",
			"black","red","green","wild"};
	public static final Color[] COLOR_NAMES = { new Color(84, 22, 180), Color.WHITE, Color.BLUE, Color.YELLOW,
			Color.ORANGE, Color.BLACK, Color.RED, Color.GREEN, new Color(255, 0, 127) };
	public static final int ARR_SIZE = 10;
	public static ArrayList<Rectangle> clickableAdd;
	public static ArrayList<Rectangle> clickableSub;
	public static ArrayList<Rectangle> clickableArrow;
	public static ArrayList<Rectangle> clickableContracts;
	public static final int shift = -50;

	public static void init() {
		clickableAdd = new ArrayList<Rectangle>(); // add card to selection
		clickableSub = new ArrayList<Rectangle>(); // subtract from train card selection
		clickableArrow = new ArrayList<Rectangle>(); // switch between contract cards
		clickableContracts = new ArrayList<Rectangle>(); // first 3 are the selectable contracts, last index is the complete seletion button
		
		int x = 90;
		for (int i = 0; i < 9; i++) {
			clickableAdd.add(new Rectangle(x + shift, 870, 50, 40));
			clickableSub.add(new Rectangle(x + 50 + shift, 870, 50, 40));
			x += 140;

		}
		// adds the turn arrows for contract viewing
		clickableArrow.add(new Rectangle(1050, 790, 35, 35));
		clickableArrow.add(new Rectangle(1315, 790, 35, 35));

		clickableContracts.add(new Rectangle(600, 760, 320, 33));
		clickableContracts.add(new Rectangle(600, 793, 320, 33));
		clickableContracts.add(new Rectangle(600, 826, 320, 33));
		clickableContracts.add(new Rectangle(920, 760, 60, 100)); // this one is the "done" button
	}

	public static void drawHand(Graphics g, Player p, ArrayList<TrainCard> chosen) {
		Graphics2D g2 = (Graphics2D) g;
		// g2.setColor(new Color(255, 105, 180));

		//sets color for bottom panel
		g2.setColor(new Color(184, 134, 11));
		g2.fillRect(0, 756, 1535, 325);
		g2.setColor(Color.BLACK);
		g2.drawRect(0, 755, 1535, 325);
		ArrayList<TrainCard> trainCard = p.getTrainCards();

		g2.setColor(DataDrawer.playerColors[GameState.PLAYER_COLORS_LIST.indexOf(p.getTrainColor())]);
		g2.setStroke(new BasicStroke(7));
		g2.drawLine(0, 759, 1535, 759);
		g2.drawLine(1530, 759, 1530, 1079);
		g2.drawLine(2, 759, 2, 1079);
		
		g2.setColor(Color.BLACK);
		int x = 90;

		for (int i = 0; i < COLOR_NAMES.length; i++) {
			
			BufferedImage card = null; // image of train cards
			try { 			
				card = ImageIO.read(new File("TrainCards/" + TRAIN_COLORS[i] + ".png"));
			}
			catch (Exception e) {}
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(6)); // draws outline box for hand card
			g2.drawRect(x + shift, 870, 50, 210);
			drawArrow(g, x + 25 + shift, 870 + 35, x + 25 + shift, 870);
			g2.drawRect(x + 50 + shift, 870, 50, 210);
			drawArrow(g, x + 75 + shift, 870, x + 75 + shift, 870 + 35);
			g2.setStroke(new BasicStroke(1));
			
			g2.setColor(COLOR_NAMES[i]); // draws body of each
			g2.fillRect(x + 1 + shift, 910, 98, 170);
			g2.drawImage(card, x + 1 + shift, 910, 98, 170, null);

			int cnt1 = getCount(TRAIN_COLORS[i], trainCard);
			int cnt2 = getCount(TRAIN_COLORS[i], chosen);
			
			g2.setColor(Color.WHITE);
			g2.setFont(new Font("Comic Sans MS", Font.PLAIN,30));
			g2.drawString((cnt1+cnt2) + "", x + 40 + shift, 1050);
			
			g2.setColor(Color.RED);
			if(cnt2 != 0)
				g2.drawString("" + cnt2, x + 40 + shift, 950);
			x += 140;
		}
		if(chosen.size() > 0) {
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(3));
			g2.drawRect(1310, 920, 150, 50);
			g2.setColor(Color.RED);
			g2.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
			g2.drawString("Chosen: " + chosen.size(), 20 +1300, 950);
		}
	}

	public static void drawContractSelection(Graphics g, ArrayList<ContractCard> selection) {

		Graphics2D g2 = (Graphics2D) g;
		/*
		g2.setStroke(new BasicStroke(6));
		g2.drawRect(600, 760, 380, 100);
		g2.setStroke(new BasicStroke(1));
		g2.setColor(Color.WHITE);
		g2.fillRect(600, 760, 380, 100);
		g2.setColor(Color.BLACK);
		g2.drawRect(600, 760, 320, 33);
		g2.drawString(selection.get(0).getCity1().getName() + " to " + selection.get(0).getCity2().getName(), 610, 780); // contract 1
		g2.drawRect(600, 793, 320, 33);
		g2.drawString(selection.get(1).getCity1().getName() + " to " + selection.get(1).getCity2().getName(), 610, 815);// contract 2
		g2.drawRect(600, 826, 320, 33);
		g2.drawString(selection.get(2).getCity1().getName() + " to " + selection.get(2).getCity2().getName(), 610, 850); // ontract 3
		*/
		int w = 170, l = 98;
		for(int i = 0; i < selection.size(); i++) {
			int serial = selection.get(i).getSerial();
			
			BufferedImage card = null; // image of contract cards
			try { 			
				card = ImageIO.read(new File(serial + ".png"));
			}
			catch (Exception e) {}
			g2.drawImage(card, 5, 760, w, l, null);
		}
	}

	public static void drawContractCards(Graphics g, ArrayList<ContractCard> cards) {
		// draws contract cards in the player's hand
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(4));
		g2.setColor(Color.BLACK);
		g2.drawRect(1100, 765, 200, 85);
		g2.setColor(Color.WHITE);
		g2.fillRect(1100, 765, 199, 84);
		// draw contents here
		g2.setColor(Color.BLACK);

		// g2.drawString(cards.get(0).toString(), 1125, 800);
		g2.setColor(Color.BLACK);

		g2.drawRect(1050, 790, 35, 35);
		g2.drawRect(1315, 790, 35, 35);
		drawArrow(g, 1085, 807, 1050, 807);
		drawArrow(g, 1315, 807, 1350, 807);

	}

	public static void advanceCard(Player p, int direction) {
		// shifts the card displaying left or right
		if (direction < 0)
			Collections.rotate(p.getContracts(), 1);
		else if (direction > 0)
			Collections.rotate(p.getContracts(), -1);
		// drawContractCards(g, p.getContracts());
	}

	private static int getCount(String string, ArrayList<TrainCard> list) {
		// gets the count of each color card.
		int count = 0;
		for (TrainCard c : list)
			if (c.getColor().equals(string))
				count++;
		return count;
	}

	public static void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
		// draws the arrow.
		Graphics2D g = (Graphics2D) g1.create();
		g.setStroke(new BasicStroke(3));
		double dx = x2 - x1, dy = y2 - y1;
		double angle = Math.atan2(dy, dx);
		int len = (int) Math.sqrt(dx * dx + dy * dy);
		AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
		at.concatenate(AffineTransform.getRotateInstance(angle));
		g.transform(at);

		// Draw horizontal arrow starting in (0, 0)
		g.drawLine(0, 0, len, 0);
		g.fillPolygon(new int[] { len, len - ARR_SIZE, len - ARR_SIZE, len }, new int[] { 0, -ARR_SIZE, ARR_SIZE, 0 },3);
	}
}
