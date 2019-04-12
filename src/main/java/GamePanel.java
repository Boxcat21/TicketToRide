import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements MouseListener{
	
	private GameState game;
	private boolean fullscreen;
	
	public GamePanel() throws IOException{ 
		//game = new GameState();
		
		setSize(1920,1080);
		setVisible(true);
		
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
		Player p = new Player("Yeet");
		HandDrawer.drawHand(g, p);
	}
	
	
}
