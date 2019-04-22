import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
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
		int counter = Integer.parseInt(scan.nextLine());
		for (int i = 0; i < counter; i++) {
			String[] temp = scan.nextLine().split("|");
			City one = null;
			City two = null;
			for (City c : cities) {
				if (c.getName().equals(temp[1]))
					one = c;
				if (c.getName().equals(temp[2]))
					two = c;
			}
			contractList.add(new ContractCard(one, two, Integer.parseInt(temp[0])));
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
		HashMap<String, Point> connectedCities = new HashMap<String, Point>();
	//Puts all of the connected cities in a hashmap with the corresponding point
		scan = new Scanner(new File("Cities"));
		while(scan.hasNextLine())
		{
			String[] temp = scan.nextLine().split(",");
			 connectedCities.put(temp[0],new Point(Integer.parseInt(temp[1]),Integer.parseInt(temp[2])));
		}
		
		/*
	
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
			
			
		}*/
		
		

		//Reads in the connected cities. and init each city with the corresponding point, name, but with an empty arraylist
		scan = new Scanner(new File("ConnecetdCities.txt"));
		HashMap<String, String> previous = new HashMap<>();
		while (scan.hasNextLine()) {
			String[] temp = scan.nextLine().split(",");
			if (previous.get(temp[1])!=null&&!((previous.get(temp[1]).equals(temp[0]) || (previous.get(temp[0]).equals(temp[1]))))) {
				//needs fix
				cities.add(new City(connectedCities.get(temp[0]),temp[0],new ArrayList<Edge>()));
			}
		}
		this.passedCities = new ArrayList<City>();
		//Making the list of edges - city value will be used from the list of cities
	
	scan = new Scanner(new File("ConnecetdCities.txt"));
	while(scan.hasNextLine())
	{
		String[] tempFirstTwo = scan.nextLine().split(",");
		String[] tempLastTwo = scan.nextLine().split("|");

		ArrayList<City> cityTemps = new ArrayList<>();
		for(City c:cities)	
			if(c.getName().equals(tempFirstTwo[0]))
				cityTemps.add(c);
		edges.add(new Edge(Integer.parseInt(tempLastTwo[0]),tempLastTwo[1],cityTemps));
		cityTemps = new ArrayList<>();
		
	}

		//Connecting all of the cities to edges; edges to cities was done above
	for(City c:cities)
	{
		ArrayList<Edge> edgeTemps = new ArrayList<>();
		for(Edge e:edges)
			{
				if(e.getCities().contains(c));
				edgeTemps.add(e);
			}
		c = new City(c.getPoint(),c.getName(),edgeTemps);
		edgeTemps = new ArrayList<>();
	}
	

	}
		
	
	
	
	

	public Player getCurPlayer() {
		return curPlayer;
	}
	public ArrayList<Edge> getEdges()
	{
		return this.edges;
	}

	public Queue<Player> getPlayerList() {
		return players;
	}
	public ArrayList<TrainCard> getDisplayCards()
	{
		return displayCards;
	}
	public ArrayList<ContractCard> getDisplayContracts()
	{
		ArrayList<ContractCard> temps  = new ArrayList<>();
		for(int i = 0; i<3; i++)
			temps.add(this.contractList.poll());
		return temps;
	}

	public void placeTrain(Edge e) {
		if (!e.getHasTrains()) {
			e.setHasTrains();
			e.setPlayer(curPlayer);
			turnCounter -= 2;
		}

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
			sharedCity1 = checkContractsHelper(sharedCity1, city1Edges, one);
			
			ArrayList<Edge> sharedCity2 = new ArrayList<>();
			sharedCity1 = checkContractsHelper(sharedCity2, city2Edges, two);
			// retainAll edges, if resulting set.isEmpty(), no path
			sharedCity1.retainAll(sharedCity2);
			if (sharedCity1.isEmpty()) 
				return;
			else // there is a path, add points
				curPlayer.addPoints(c.getNumPoints());
			// add method to make sure contracts are added to player, also setComplete
		}
		
	}
	
	private ArrayList<Edge> checkContractsHelper(ArrayList<Edge> shared, ArrayList<Edge> cityEdges, City start) {
		if ( start == null || cityEdges == null) 
			return shared;
		else {
			Edge current = new Edge();
			for ( Edge e  : cityEdges ) 
				if ( e.getCities().contains(start))
					current = e;
			cityEdges.remove(current);
			return checkContractsHelper(shared, cityEdges, current.getOtherCity(start));
		}
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

	public String chooseContractCard(ArrayList<Integer> choices)
	{
		if(choices.size()<1)
			return "Invalid. You must choose at least one contract.";
		for(int i=0; i<3;i++)
			if(i==choices.get(i))
				this.contractList.offer(this.getDisplayContracts().get(i));
		turnCounter -=2;
		return "Successful";
		
			
		
			
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
				ArrayList<City> endingEdgeCities1;
				ArrayList<City> endingEdgeCities2;
				if(longest.indexOf(start) > longest.size()/2) {
					endingEdgeCities1 = longest.get(0).getCities();
					endingEdgeCities2 = longest.get(1).getCities();
				}
				else {
					endingEdgeCities1 = longest.get(longest.size()-1).getCities();
					endingEdgeCities2 = longest.get(longest.size()-2).getCities();
				}
				
				if(endingEdgeCities2.contains(endingEdgeCities1.get(0)))
					newStart = endingEdgeCities1.get(1);
				else
					newStart = endingEdgeCities1.get(0);
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
		
		ArrayList<ArrayList<Edge>> paths = new ArrayList<ArrayList<Edge>>();
		for(int i = 0; i < edges.size(); i++) {
			//paths.add(longestPathRec)
		}
		
		return null;
	}
	public String mostContractCards() {
		//Cole (DONE, untested)
		Player p = players.peek();
		Player highest = players.peek();
		
		for(int i = 0; i <= 4; i++)
		{
			p = players.poll();
			if(p.getCompleted().size() > highest.getCompleted().size())
			{
				highest = p;
			}
		}
		return p.toString();
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
