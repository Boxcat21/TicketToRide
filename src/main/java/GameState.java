import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
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
			"Green", "Rainbow"};
	public static final String[] PLAYER_COLORS = {"Red", "Green", "Yellow", "Blue"};
	private String longestPath;
	private String mostContracts;
	//for longest path
	private ArrayList<City> passedCities;
	
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
		for(int i = 0; i < this.PLAYER_COLORS.length; i++) 
			players.add(new Player(PLAYER_COLORS[i]));
		curPlayer = players.poll();
		turnCounter = 2;
		// edges and cities
		cities = new ArrayList<>();
		edges = new ArrayList<>();
		//Puts all of the connected cities in a hashmap with the corresponding point
		HashMap<String, Point> connectedCities = new HashMap<String, Point>();
		scan = new Scanner(new File("Cities.txt"));
		while(scan.hasNextLine())
		{
			String[] temp = scan.nextLine().split(",");
			 connectedCities.put(temp[0],new Point(Integer.parseInt(temp[1]),Integer.parseInt(temp[2])));
		}
		//reads in the edges: ensures no repeats
		scan = new Scanner(new File("ConnectedCities.txt"));
		HashMap<String, ArrayList<Edge>> edgeTemps = new HashMap<>();
		while(scan.hasNextLine())
		{
			String[] line = scan.nextLine().split(",");
			ArrayList<Edge> cityTemps = new ArrayList<Edge>();
			if(edgeTemps.get(line[0])==null)
			{
				edgeTemps.put(line[0],cityTemps);
			}
			
			cityTemps.add(new Edge(Integer.parseInt(line[2]),line[3],null));
			edgeTemps.put(line[0],null);
			
			
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
		this.passedCities = new ArrayList<City>();
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
		checkContracts();
	}

	private void checkContracts() {
		ArrayList<ContractCard> contracts = curPlayer.getContracts();
		for ( ContractCard c : contracts) {
			City one = c.getCity1();
			City two = c.getCity2(); // are the references correct?
			
			ArrayList<Edge> city1Edges = one.getEdges(curPlayer.getTrainColor());
			ArrayList<Edge> city2Edges = two.getEdges(curPlayer.getTrainColor());
			
			if ( city1Edges.isEmpty() || city2Edges.isEmpty() ) // no path, nothing to find here! 
				return;
			
			// calls traversals from each city, storing the added edges
			ArrayList<Edge> sharedCity1 = new ArrayList<>();
			sharedCity1 = checkContractsHelper(sharedCity1, one);
			
			ArrayList<Edge> sharedCity2 = new ArrayList<>();
			sharedCity1 = checkContractsHelper(sharedCity2, two);
			// retainAll edges, if resulting set.isEmpty(), no path
			sharedCity1.retainAll(sharedCity2);
			if (sharedCity1.isEmpty()) 
				return;
			else // there is a path, add points
				curPlayer.addPoints(c.getNumPoints());
			
		}
		
	}
	
	private ArrayList<Edge> checkContractsHelper(ArrayList<Edge> shared, City start) {
		
		return shared;
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
		Collections.sort(winnerPoints, (p1, p2) -> Integer.compare(p1.getPoints(), p2.getPoints()));
		// sorts the winners by order of points
		return winnerPoints;
		
	}

	public void endTurn() {
		Player temp = players.poll();
		// goes to next player
		players.offer(curPlayer);
		curPlayer = temp;
		
		turnCounter = 2;
		
	}
	public String getMostContracts() {
		return mostContracts;
	}

	public String longestPath() { //THINGS TO DO: Check for sketchy case, do the recursion
		ArrayList<City> startCities = this.cities;
		
		int[] longestCntPerPlyr = {0,0,0,0};
		
		for(int n = 0; n < longestCntPerPlyr.length; n++) { //COUNT = N
			while(startCities.size() > 0) {
				City start = startCities.get(0);
				ArrayList<Edge> longest = longestPathRecur(start, new ArrayList<Edge>(), PLAYER_COLORS[n]);
				
				//FIX FINDING END CITY ASAP - for future SID, cause current sid lazy af
				City newStart = null;
				ArrayList<City> temp1;
				ArrayList<City> temp2;
				if(longest.indexOf(start) > longest.size()/2) {
					temp1 = longest.get(0).getCities();
					temp2 = longest.get(1).getCities();
				}
				else {
					temp1 = longest.get(longest.size()-1).getCities();
					temp2 = longest.get(longest.size()-2).getCities();
				}
				
				if(temp2.contains(temp1.get(0)))
					newStart = temp1.get(1);
				else
					newStart = temp1.get(0);
				//ASUMING NEW START IS RIGHT :: I THINK IT WAS FIXED - future sid
				longest = longestPathRecur(newStart, new ArrayList<Edge>(), PLAYER_COLORS[n]);
				
				for(int i = 0; i < longest.size(); i++) {
					longestCntPerPlyr[n] += longest.get(i).getLength();
				}
				for(int i = 0; i < startCities.size(); i++) {
					if(passedCities.contains(startCities.get(i))) {
						startCities.remove(i);
						i--;
					}
				}
			}
		}
		
		return "";
	}
	public ArrayList<Edge> longestPathRecur(City c, ArrayList<Edge> passedEdges, String color) {
		ArrayList<Edge> edges = c.getEdges(color);
		
		return null;
	}
	public String mostContractCards() {
		//Cole (NOT DONE)
		int temp = 0;
		Player highest;
		Player p;
		
		for(int i = 0; i <= 4; i++)
		{
			p = players.poll();
			for(i = 0; i <= p.getContracts().size(); i++)
			{
				if(p.getContracts().get(i).isComplete())
				{
					temp++;
				}
			}
			
			//if()
		}
		return "";
	}

	public void checkTurn() { 
		if ( checkWilds()) { // if there are 3+ wild cards in the deck
			discardTrainCard.addAll(displayCards);
			displayCards.clear();
			Collections.shuffle(trainCardDeck);
			for ( int i = 0; i < 5; i++ )
				displayCards.add(trainCardDeck.pop());
		}
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
		checkContracts();
		return;
	}

	private boolean checkWilds() {
		int count = 0;
		for ( TrainCard t : displayCards) {
			if ( t.getColor().equals("Rainbow"))
				count++;
			if ( count == 3) 
				return true;
		}
		return false;
	}
}
