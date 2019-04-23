import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Runner {
	public static void main(String[] args) throws FileNotFoundException {
		JFrame j = new JFrame();
		j.setBounds(0, 0, 1920, 1080);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		j.setUndecorated(true);
		j.setVisible(true);
		
		BoardDrawer.drawBoard(j.getGraphics(), new ArrayList<City>(), new ArrayList<Edge>());
	}
}
