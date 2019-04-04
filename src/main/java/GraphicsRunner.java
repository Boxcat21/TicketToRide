import java.io.IOException;

import javax.swing.JFrame;

public class GraphicsRunner extends JFrame{
	
	public GraphicsRunner(String name) throws IOException{
		super(name);
		add(new GamePanel());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1920,1080);
//		setUndecorated(true);
		setVisible(true);
		
	}
	public static void main(String[] args) throws IOException{
		
		new GraphicsRunner("Yeet");
		
	}
}
