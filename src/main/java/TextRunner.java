import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

public class TextRunner {
	static GameState g;
	static Scanner scan;

	public static void main(String[] args) throws FileNotFoundException {
		// Instantiating the game
		g = new GameState();
		// Going through the game
		scan = new Scanner(System.in);

		int cnt = 0;
		//Each player gets to choose their contracts from five at the beginning of the round
		for(int i=0; i<4;i++)
		{
			System.out.println(g.getCurPlayer().toString());
			testDrawContract(true);
		}
		
		// Starting each turn
		while (!g.isEnded()) {
			System.out.println(g.getCurPlayer().toString());
			if(cnt == 0) {
				System.out.println("Enter your choice of contracts: ");
				
				
				
			}
			System.out.println(", enter d to draw cards, enter c to draw contracts, enter p to place train");
			// Drawing cards
			String s = scan.nextLine();
			if (s.equals("d"))
				testDrawTrainCards();

			// Drawing contracts
			else if (s.equals("c")) {
				testDrawContract(false);
			}
			// Placing trains
			else if (s.equals("p")) {
				testPlaceTrains();
			}

		}

	}

	public static void testDrawTrainCards() {
		Player current = g.getCurPlayer();
		while (current.getTrainColor().equals(g.getCurPlayer().getTrainColor())) {
			System.out.println("If you want face up cards, press f. If not press something else lmao");
			ArrayList<TrainCard> display = g.getDisplayCards();
			if (scan.nextLine().equals("f")) {
				for (TrainCard c : display)
					System.out.print(c.getColor() + " ");
				System.out.println("choose the card you want. enter the corresponding index starting from 0");
				g.chooseTrainCard(Integer.parseInt(scan.nextLine()));

			} else
				g.drawTrainCard();
		}
	}

	public static void testDrawContract(boolean b) 
	{
		int nums = 3;
		if(b)
			nums = 5;
		ArrayList<ContractCard> c = g.drawContracts(nums);
		for (int i = 0; i < c.size(); i++)
			System.out.println(c.get(i).getCity1() + " to " + c.get(i).getCity2());
		System.out.println(
				"Put in each number the numbers of the cards you want in the corresponding order of the display and seperate by comma");
		String[] arr = scan.nextLine().split(",");
		ArrayList<Integer> choices = new ArrayList<>();
		for (String str : arr)
			choices.add(Integer.parseInt(str));
		g.chooseContractCard(choices,c.size());

	}

	public static void testPlaceTrains() {

		ArrayList<TrainCard> trains = g.getCurPlayer().getTrainCards();
		System.out.println("These are your available trains; enter the indexes of the ones you want starting from 0");
		for (TrainCard t : trains)
			System.out.println(t);
		String[] input = scan.nextLine().split(",");
		ArrayList<TrainCard> choices = new ArrayList<>();
		for (String str : input)
			choices.add(trains.get(Integer.parseInt(str)));
		System.out
				.println("Enter the two cities with commas that you want to place your train on - any order is fine.");
		String[] arr = scan.nextLine().split(",");
		System.out.println(arr.toString());
		ArrayList<Edge> edges = g.getEdges();
		for (Edge e : edges) {
			if(e.getCities().contains(arr[0])&&e.getCities().contains(arr[1]))
				g.placeTrain(e, choices);
		}

	}

}
