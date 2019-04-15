import java.io.FileNotFoundException;

import javax.swing.JFrame;

public class Runner {
	public static void main(String[] args) throws FileNotFoundException {
		BoardDrawer b = new BoardDrawer();
		
		JFrame j = new JFrame();
		j.setBounds(-5, -10, 1920, 1080);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j.setVisible(true);
	}
}
