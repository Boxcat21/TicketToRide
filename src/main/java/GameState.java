import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class GameState {
	private Queue<ContractCard> contractList;
	private Stack<TrainCard> trainCardDeck;
	private ArrayList<TrainCard> discardTrainCard;
	private ArrayList<TrainCard> displayCards;
	private Queue<Player> players;
	private Player curPlayer;
	private int turnCounter;
	private ArrayList<Edge> edges;
	private ArrayList<City> cities;
	public static final String[] TRAIN_COLORS = { "Purple", "White", "Blue", "Yellow", "Orange", "Black", "Red",
			"Green" };
	private String longestPath;
	private String mostContracts;
	
	public GameState() throws FileNotFoundException {
		// Reading in contracts
		cities = new ArrayList<>();
		contractList = new LinkedList<>();
		longestPath = "";
		mostContracts = "";
		Scanner scan = new Scanner(new File("tickets.txt"));
		for (int i = 0; i < Integer.parseInt(scan.nextLine()); i++) {
			String[] temp = scan.nextLine().split("|");
			City one = null;
			City two = null;
			for (City c : cities) {
				if (c.getName().equals(temp[0]))
					one = c;
				if (c.getName().equals(temp[1]))
					two = c;
			}
			contractList.add(new ContractCard(one, two, Integer.parseInt(temp[2])));
		}
		// Adding train cards
		ArrayList<TrainCard> list = new ArrayList<TrainCard>();
		trainCardDeck = new Stack();
		for (int j = 0; j < 8; j++)
			for (int i = 0; i < 12; i++)
				trainCardDeck.add(new TrainCard(this.TRAIN_COLORS[j]));

		for (int i = 0; i < 14; i++)
			trainCardDeck.add(new TrainCard("Wild"));
		Collections.shuffle(trainCardDeck);
		// discard list
		discardTrainCard = new ArrayList<>();
		// display cards
		displayCards = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			displayCards.add(trainCardDeck.pop());
		}

		// adding players, cur player and turncounter
		players = new LinkedList<>();
		players.add(new Player("red"));
		players.add(new Player("green"));
		players.add(new Player("yellow"));
		players.add(new Player("blue"));
		curPlayer = players.poll();
		turnCounter = 2;
		// edges and cities
		cities = new ArrayList<>();
		//Puts all of the connecteed cities in a hashmap with the corresponding point
		HashMap<String, Point> connectedCities = new HashMap<String, Point>();
		scan = new Scanner(new File("CityPoints.txt"));
		while(scan.hasNextLine())
		{
			String[] temp = scan.nextLine().split(",");
			 connectedCities.put(temp[0],new Point(Integer.parseInt(temp[1]),Integer.parseInt(temp[2])));
		}
		//Reads in the connected cities
		scan = new Scanner(new File("ConnectedCities.txt"));
		HashMap<String, String> previous = new HashMap<>();

		while (scan.hasNextLine()) {
			String[] temp = scan.nextLine().split(",");
			if (!((previous.get(temp[1]).equals(temp[0]) || (previous.get(temp[0]).equals(temp[1]))))) {
				//needs fix
				cities.add(new City(connectedCities.get(temp[0]),temp[0],new ArrayList<>()));
			}
		}
	}

	public Player getCurPlayer() {
		return curPlayer;
	}

	public Queue<Player> getPlayerList() {
		return players;
	}

	public void placeTrain(Edge e) {
		if (!e.getHasTrains())
			e.setHasTrains();
		turnCounter -= 2;
		checkTurn();
	}

	public boolean chooseTrainCard(int choice) {
		TrainCard t = displayCards.remove(choice);

		if (t.getColor().equals("rainbow")) {
			turnCounter -= 2;
			if (turnCounter < 0)
				return false;
		} else
			turnCounter--;

		curPlayer.addTrainCard(t);

		checkTurn();

		return true;
	}

	public void drawTrainCard() {
		curPlayer.addTrainCard(trainCardDeck.pop());
	}

	public String chooseContractCard(ArrayList<Integer> choices) {
		return "";
	}

	public ArrayList<Player> endGame() { 
		turnCounter = 0;
		longestPath = longestPath();
		mostContracts = mostContractCards();
		ArrayList<Player> winnerPoints = new ArrayList<>();
		winnerPoints.addAll(players);
		Collections.sort(winnerPoints, new Comparator<Player>() {
			@Override
			public int compare(Player p1, Player p2) {
				return p1.getPoints() - p2.getPoints();
			}
		}); // sorts the winners by order of points
		return winnerPoints;
		
	}

	public void endTurn() {
		Player temp = players.poll();
		// goes to next player
		players.offer(curPlayer);
		curPlayer = temp;
		
		turnCounter = 2;
		
	}
	public String getLongestPath() {
		return longestPath;
	}
	public String getMostContracts() {
		return mostContracts;
	}

	public String longestPath() {
		ArrayList<City> startCities = this.cities;
		
		while(startCities.size() > 0) {
			ArrayList<Edge> longest = longestPathRecur(startCities.get(0));
			longest.indexOf(startCities.get(0));
			
		}
		
		return "";
	}
	public ArrayList<Edge> longestPathRecur(City c) {
		return null;
	}
	public String mostContractCards() {
		//Abhinav
		return "";
	}

	public void checkTurn() {
		boolean lastRound = false;
		for ( Player p : players )  
			if ( p.getTrains() < 3) {
				lastRound = true;
				break;
			}
		
		if (this.turnCounter == 0 && !lastRound) {
			endTurn(); 
			return;
		}
		if ( lastRound ) //still need to fix to run one more round before ending 
			endGame();
		return;
	}
}
