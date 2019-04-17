import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
public class TextRunner {
	static GameState g;
	static Scanner scan;
	public static void main(String[]args) throws FileNotFoundException 
	{
		//Instantiating the game
		 g = new GameState();
		//Going through the game
		 scan = new Scanner(System.in);
		 
		 //Starting each turn
		System.out.println("Enter d to draw cards, enter c to draw contracts, enter p to place train");
		//Drawing cards
		if(scan.nextLine().equals("d"))
			testDrawTrainCards();

		//Drawing contracts
		else if(scan.nextLine().equals("c"))
		{
			testDrawContract();
		}
		//Placing trains
		else if(scan.nextLine().equals("p"))
		{
			testPlaceTrains();
		}
		
		
	}
	public static void testDrawTrainCards()
	{
		Player current = g.getCurPlayer();
		while(current.getTrainColor().equals(g.getCurPlayer().getTrainColor()))
		{
			System.out.println("If you want face up cards, press f. If not press something else lmao");
		ArrayList<TrainCard> display = g.getDisplayCards(); 
		if(scan.nextLine().equals("f"))
		{
			for(TrainCard c:display)
				System.out.print(c.getColor()+" ");
		System.out.println("choose the card you want. enter the corresponding index starting from 0");
		g.chooseTrainCard(Integer.parseInt(scan.nextLine()));
		
		}
		else 
			g.drawTrainCard();
		}
		
			
		
		
	}

	public static void testDrawContract()
	{
		ArrayList<ContractCard> c = g.getDisplayContracts();
		for(int i = 0; i<c.size();i++)	
		System.out.println(c.get(i).getCity1()+ " to "+c.get(i).getCity2());
		System.out.println("put in each number the numbers of the cards you want in the corresponding order of the display and seperate by comma");
		String[] arr = scan.nextLine().split(",");
		ArrayList<Integer> choices = new ArrayList<>();
		for(String str:arr)
			choices.add(Integer.parseInt(str));
		g.chooseContractCard(choices);
			
	}
	public static void testPlaceTrains()
	{
		
	}
	

}
