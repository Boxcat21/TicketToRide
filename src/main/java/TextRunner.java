import java.io.FileNotFoundException;
import java.util.Scanner;

public class TextRunner {
	static GameState g;
	public static void main(String[]args) throws FileNotFoundException 
	{
		//Instantiating the game
		 g = new GameState();
		//Going through the game
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter d to draw cards, enter c to draw contracts, enter p to place train");
		if(scan.nextLine().equals("d"))
		{
			testDrawTrainCards();
		}
		else if(scan.nextLine().equals("c"))
		{
			testDrawContract();
		}
		else if(scan.nextLine().equals("p"))
		{
			testPlaceTrains();
		}
		
		
	}
	public static void testDrawTrainCards()
	{
	 
	}
	public static void testDrawContract()
	{
		
	}
	public static void testPlaceTrains()
	{
		
	}
	

}
