import java.io.IOException;

import javax.swing.JFrame;

public class Runner {
	public static void main(String[] args) throws IOException {
		JFrame j = new JFrame();
		j.setBounds(0, 0, 1920, 1080);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j.setExtendedState(JFrame.MAXIMIZED_BOTH);
		j.setUndecorated(true);
		j.setVisible(true);

		GamePanel g = new GamePanel();

	}
}
