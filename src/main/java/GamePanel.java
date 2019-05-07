import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements MouseListener{
	
	private GameState game;
	private ArrayList<Integer> clickedEdgeIndecies;
	public GamePanel() throws IOException{ 
		setSize(1920,1080);
		setVisible(true);
		addMouseListener(this);
		
		game = new GameState();
		clickedEdgeIndecies = new ArrayList<Integer>();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		repaint();
	}
	@Override
	public void mouseEntered(MouseEvent e) {	}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {
		int index = BoardDrawer.edgeClick(e, game);
		if(index != -1) {
			clickedEdgeIndecies.add(index);
			this.repaint();
		}
		
		for (Rectangle rec : HandDrawer.clickableAdd) {

			if (rec.contains(e.getPoint()))
				//System.out.println(1); // adds 1 train card
			repaint();
		}
		for (Rectangle rec : HandDrawer.clickableSub) {

			if (rec.contains(e.getPoint()))
				//System.out.println(-1); // subtracts 1 card
			repaint();
		}
		
		if ( HandDrawer.clickableArrow.get(0).contains(e.getPoint())) {
			HandDrawer.advanceCard(p, -1);
			//System.out.println(-2);
			repaint();
		}
		if ( HandDrawer.clickableArrow.get(1).contains(e.getPoint())) { 
			HandDrawer.advanceCard(p, 1);
			//System.out.println(2);
			repaint();
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {}
	
	@Override
	public void paintComponent(Graphics g) {
		BoardDrawer.drawBoard(g, this.clickedEdgeIndecies);
		HandDrawer.drawHand(g, game.getCurPlayer());
		HandDrawer.drawContractSelection(g, /*game.getDisplayContracts()*/ new ArrayList<ContractCard>());
		HandDrawer.drawContractCards(g, game.getCurPlayer().getContracts());
		
		g.setColor(new Color(184, 134,11));
		g.fillRect(1535, 0, 1920-1535, 1080);
		g.setColor(Color.BLACK);
		g.drawRect(1535, 0, 1920-1535, 1080);
		
		HandDrawer.drawPlayer(g, game.getCurPlayer());
		
		
		
		DataDrawer.drawDisplayCards(g, game.getCurPlayer().getTrainCards());
		DataDrawer.drawCurPlayer(g, game.getCurPlayer());
	}
	
	
}
