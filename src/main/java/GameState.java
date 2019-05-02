import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeMap;

public class GameState {
	// i made this public, neccessary for graphics so the whole datastructure doesnt
	// need to be remade, can just refer to the index
	private ArrayList<Edge> edges;
	private Queue<ContractCard> contractList;
	private Stack<TrainCard> trainCardDeck;
	private ArrayList<TrainCard> discardTrainCard;
	private ArrayList<TrainCard> displayCards;
	private Queue<Player> players;
	private Player curPlayer;
	private int turnCounter;
	private ArrayList<City> cities;
	public static final String[] TRAIN_COLORS = { "Purple", "White", "Blue", "Yellow", "Orange", "Black", "Red",
			"Green" };
	public static final String[] PLAYER_COLORS = { "Red", "Green", "Yellow", "Blue" };
	private String longestPath;
	private String mostContracts;
	// for longest path
	private ArrayList<City> passedCities;

	public GameState() throws FileNotFoundException {
		// Reading in contracts

		cities = new ArrayList<>();
		contractList = new LinkedList<>();
		longestPath = "";
		mostContracts = "";
		this.passedCities = new ArrayList<City>();
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
		for (int i = 0; i < this.PLAYER_COLORS.length; i++)
			players.add(new Player(PLAYER_COLORS[i]));
		curPlayer = players.poll();
		turnCounter = 2;
		/*
		 * 
		 * 
		 * Cities and Edges
		 * 
		 * 
		 * 
		 */

		// edges and cities
		cities = new ArrayList<>();
		edges = new ArrayList<>();
		// adds all the cities with null edge arraylist
		scan = new Scanner(new File("Cities"));
		while (scan.hasNextLine()) {
			String[] temp1 = scan.nextLine().split(",");
			cities.add(new City(new Point(Integer.parseInt(temp1[1]), Integer.parseInt(temp1[2])), temp1[0],
					new ArrayList<Edge>()));

		}

		// At this point each city is initialized with the correct name and point of the
		// city.
		// The only thing empty for each city is the arraylist inside; it's initialized
		// but it is empty

		// Puts all the cities and their connections in a hashmap of string, list
		HashMap<String, ArrayList<String>> cityConnections = new HashMap<>();
		scan = new Scanner(new File("ConnectedCities.txt"));
		while (scan.hasNextLine()) {
			String line1 = scan.nextLine();
			String[] tempFirstTwo = line1.substring(0, line1.indexOf('|')).split(",");
			String key = tempFirstTwo[0];
			if (cityConnections.containsKey(key)) {
				ArrayList<String> temp = cityConnections.get(key);
				temp.add(tempFirstTwo[1]);
			} else {
				ArrayList<String> temp = new ArrayList<>();
				temp.add(tempFirstTwo[1]);
				cityConnections.put(key, temp);
			}

		}


		// Make the list of edges
		ArrayList<String> strings = new ArrayList<>();
		HashMap<City, City> previous = new HashMap<>();
		scan = new Scanner(new File("ConnectedCities.txt"));
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			String[] tempFirstTwo = line.substring(0, line.indexOf('|')).split(",");
			String[] tempLastTwo = line.substring(line.indexOf('|') + 1, line.length()).split(",");
			ArrayList<City> tempCities = new ArrayList<>();
			String key = tempFirstTwo[0];
			String value = tempFirstTwo[1];
			for (String s : cityConnections.get(key))// for every city that is connected to the city called "key"
			{
				City c = cityHelper(s);
				City orig = cityHelper(key);
				if (value.equals(s) && !previous.containsKey(orig))// if the second city is the same as the city that is													// currently "s", one of the cities connected to c
				{
					tempCities.add(new City(c.getPoint(), s, new ArrayList<Edge>()));
					tempCities.add(new City(orig.getPoint(), key, new ArrayList<Edge>()));
					previous.put(orig, c);
					previous.put(c, orig);

					edges.add(new Edge(Integer.parseInt(tempLastTwo[0]), tempLastTwo[1], tempCities));
				}
			}

			tempCities = new ArrayList<>();

		}
		
		scan = new Scanner(new File("Doubles.txt"));
		previous = new HashMap<>();
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			String[] tempFirstTwo = line.substring(0, line.indexOf('|')).split(",");
			String[] tempLastTwo = line.substring(line.indexOf('|') + 1, line.length()).split(",");
			ArrayList<City> tempCities = new ArrayList<>();
			String key = tempFirstTwo[0];
//			ArrayList<Edge> tempEdges = new ArrayList<Edge>();
			String value = tempFirstTwo[1];
			for (String s : cityConnections.get(key))// for every city that is connected to the city called "key"
			{
				City c = cityHelper(s);
				City orig = cityHelper(key);
				if (value.equals(s)&& !previous.containsKey(orig))// if the second city is the same as the city that is													// currently "s", one of the cities connected to c
				{
					tempCities.add(new City(c.getPoint(), s, new ArrayList<Edge>()));
					tempCities.add(new City(orig.getPoint(), key, new ArrayList<Edge>()));

					previous.put(orig, c);
					previous.put(c, orig);
					edges.add(new Edge(Integer.parseInt(tempLastTwo[0]), tempLastTwo[1], tempCities));
				}
			}

			tempCities = new ArrayList<>();

		}

		


		for (int i = cities.size() - 1; i >= 0; i--) {
			ArrayList<Edge> edgeTemps = new ArrayList<>();
			for (Edge e : edges) {
				if (e.getCities().contains(cities.get(i)))
					;
				{
					edgeTemps.add(e);
				}

			}
			City oldCity = cities.remove(i);
			cities.add(i, new City(oldCity.getPoint(), oldCity.getName(), edgeTemps));
			edgeTemps = new ArrayList<>();
		}

	}
	private City cityHelper(String name) {
		for (City c : cities)
			if (c.getName().equals(name))
				return c;
		return null;
	}



	public Player getCurPlayer() {
		return curPlayer;
	}

	public ArrayList<Edge> getEdges() {
		return this.edges;
	}

	public Queue<Player> getPlayerList() {
		return players;
	}

	public ArrayList<TrainCard> getDisplayCards() {
		return displayCards;
	}

	public ArrayList<ContractCard> getDisplayContracts() {
		ArrayList<ContractCard> temps = new ArrayList<>();
		for (int i = 0; i < 3; i++)
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
		for (ContractCard c : contracts) {
			City one = c.getCity1();
			City two = c.getCity2(); // are the references correct?

			ArrayList<Edge> city1Edges = one.getEdges(curPlayer.getTrainColor());
			ArrayList<Edge> city2Edges = two.getEdges(curPlayer.getTrainColor());

			if (city1Edges.isEmpty() || city2Edges.isEmpty()) // no path, nothing to find here!
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
		if (start == null || cityEdges == null)
			return shared;
		else {
			Edge current = new Edge();
			for (Edge e : cityEdges)
				if (e.getCities().contains(start))
					current = e;
			cityEdges.remove(current);
			return checkContractsHelper(shared, cityEdges, current.getOtherCity(start));
		}
	}

	public boolean chooseTrainCard(int choice) {
		TrainCard t = displayCards.get(choice);
		 if (t.getColor().equals("Wild") && turnCounter != 1) {
			 t = displayCards.remove(choice);
			curPlayer.addTrainCard(t);
			displayCards.add(trainCardDeck.pop());
			turnCounter -= 2;
		} else if(!t.getColor().equals("Wild")) {
			 t = displayCards.remove(choice);
			curPlayer.addTrainCard(t);
			displayCards.add(trainCardDeck.pop());
			turnCounter--;
			}
		else {
			System.out.println("Stop right there, criminal scum!");
		}
		checkTurn();
		System.out.println(turnCounter);
		System.out.println(curPlayer.getTrainCards());
		return true;
	}

	public void drawTrainCard() {
		curPlayer.addTrainCard(trainCardDeck.pop());
	}

	public String chooseContractCard(ArrayList<Integer> choices) {
		if (choices.size() < 1)
			return "Invalid. You must choose at least one contract.";
		for (int i = 0; i < 3; i++)
			if (i == choices.get(i))
				this.contractList.offer(this.getDisplayContracts().get(i));
		turnCounter -= 2;
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

	public String longestPath() { // THINGS TO DO: Check for sketchy case, do the recursion
		ArrayList<City> startCities = this.cities;

		int[] longestCntPerPlyr = { 0, 0, 0, 0 };

		for (int n = 0; n < longestCntPerPlyr.length; n++) { // COUNT = N
			while (startCities.size() > 0) {
				City start = startCities.get(0);
				ArrayList<Edge> longest = longestPathRecur(start, new ArrayList<Edge>(), PLAYER_COLORS[n]);

				// FIX FINDING END CITY ASAP - for future SID, cause current sid lazy af
				City newStart = null;
				ArrayList<City> endingEdgeCities1;
				ArrayList<City> endingEdgeCities2;
				if (longest.indexOf(start) > longest.size() / 2) {
					endingEdgeCities1 = longest.get(0).getCities();
					endingEdgeCities2 = longest.get(1).getCities();
				} else {
					endingEdgeCities1 = longest.get(longest.size() - 1).getCities();
					endingEdgeCities2 = longest.get(longest.size() - 2).getCities();
				}

				if (endingEdgeCities2.contains(endingEdgeCities1.get(0)))
					newStart = endingEdgeCities1.get(1);
				else
					newStart = endingEdgeCities1.get(0);
				// ASUMING NEW START IS RIGHT :: I THINK IT WAS FIXED - future sid
				longest = longestPathRecur(newStart, new ArrayList<Edge>(), PLAYER_COLORS[n]);

				for (int i = 0; i < longest.size(); i++) {
					longestCntPerPlyr[n] += longest.get(i).getLength();
				}
				for (int i = 0; i < startCities.size(); i++) {
					if (passedCities.contains(startCities.get(i))) {
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
		for (int i = 0; i < edges.size(); i++) {
			// paths.add(longestPathRec)
		}

		return null;
	}

	public String mostContractCards() {
		// Cole (DONE, untested)
		Player p = players.peek();
		Player highest = players.peek();

		for (int i = 0; i <= 4; i++) {
			p = players.poll();
			if (p.getCompleted().size() > highest.getCompleted().size()) {
				highest = p;
			}
		}
		return p.toString();
	}

	public void checkTurn() {//does not continue to next round, due to the turn counter
		if (checkWilds()) { // if there are 3+ wild cards in the deck
			discardTrainCard.addAll(displayCards);
			displayCards.clear();
			Collections.shuffle(trainCardDeck);
for (int i = 0; i < 5; i++)
				displayCards.add(trainCardDeck.pop());
		}
		boolean lastRound = false;
for (Player p : players)
			if (p.getTrains() < 3) {
				lastRound = true;
				break;
			}

		
		 if (turnCounter <= 0 && !lastRound) { endTurn(); return; }
		 
		if (lastRound) // still need to fix to run one more round before endin
			endGame();
		checkContracts();
		return;
	}

	private boolean checkWilds() {
		int count = 0;
		for (TrainCard t : displayCards) {
			if (t.getColor().equals("Rainbow"))
				count++;
			if (count == 3)
				return true;
		}
		return false;
	}
}
