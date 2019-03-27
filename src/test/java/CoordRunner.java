
import java.awt.*;
	import java.awt.event.*;
	import javax.swing.*;

	
	public class CoordRunner extends JFrame
	{


	   public CoordRunner(String frameName)
	       {
	       super(frameName);
	       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	       setSize(1920,1080);
	       add(new CityCoordInputs());
	       setVisible(true);
	       setResizable(false);
	       }

	       public static void main(String args[])
	       {   CoordRunner game = new CoordRunner("Tester");
	       }
	}


