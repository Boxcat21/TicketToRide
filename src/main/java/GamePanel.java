import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements MouseListener{
	
	private GameState game;
	private boolean fullscreen;
	Player p; // temporary
	
	public GamePanel() throws IOException{ 
		//game = new GameState();
		 p = new Player("Yeet"); // temporary testing player
		p.addContractCard(new ContractCard(new City(null, "Elleh1", null), new City(null, "Elleh2", null), 20));
		p.addContractCard(new ContractCard(new City(null, "Elleh2", null), new City(null, "Elleh3", null), 20));
		p.addContractCard(new ContractCard(new City(null, "Elleh3", null), new City(null, "Elleh4", null), 20));
		setSize(1920,1080);
		setVisible(true);
		addMouseListener(this);
		
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println(e.getPoint());
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	
			
		
	}
	@Override
	public void paintComponent(Graphics g) {
//		Graphics2D g2 = (Graphics2D) g;
//		g2.drawRect(0, 0, 1440, 720);
//		g2.setColor(Color.ORANGE);
//		g2.fillRect(1441, 0, 480, 1080);
//		g2.fillRect(0, 721, 1920, 360);
		
		HandDrawer.drawHand(g, p);
		HandDrawer.drawContractSelection(g, /*game.getDisplayContracts()*/ new ArrayList<ContractCard>());
		HandDrawer.drawContractCards(g, p.getContracts());
		HandDrawer.advanceCard(g, p,1); // changes top contract card
		
	}
	
	
}
