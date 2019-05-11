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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeMap;

public class GameState {
	// i made this public, neccessary for graphics so the whole datastructure doesnt
	// need to be remade, can just refer to the index
	public static final String[] TRAIN_COLORS = { "magenta", "white", "blue", "yellow", "orange", "black", "red",
			"green", "wild" };
	public static final String[] PLAYER_COLORS = { "Red", "Green", "Yellow", "Blue" };
	public static final ArrayList<String> TRAIN_COLORS_LIST = new ArrayList<String>();
	public static final ArrayList<String> PLAYER_COLORS_LIST = new ArrayList<String>();
	private Map<Integer, Integer> routePoints = new HashMap<Integer, Integer>();
	private Queue<Player> players;
	private Queue<ContractCard> contractDeck;
	private Stack<TrainCard> trainCardDeck;
	private ArrayList<TrainCard> discardTrainCard;
	private ArrayList<TrainCard> displayCards;
	private ArrayList<City> cities;
	private ArrayList<City> passedCities;
	private ArrayList<ContractCard> displayContracts;
	private ArrayList<Edge> edges;
	private Player lastPlayer;
	private Player curPlayer;
	private int turnCounter;
	private boolean lastRound;
	private boolean isEnded;
	private boolean choosingContracts;
	private String longestPath;
	private String mostContracts;

	public GameState() throws FileNotFoundException {
		// points map
		routePoints.put(1, 1);
		routePoints.put(2, 2);
		routePoints.put(3, 4);
		routePoints.put(4, 7);
		routePoints.put(5, 10);
		routePoints.put(6, 15);
		// static variables
		for (String s : TRAIN_COLORS)
			TRAIN_COLORS_LIST.add(s);
		for (String s : PLAYER_COLORS)
			PLAYER_COLORS_LIST.add(s);
		// globals
		isEnded = false;
		lastPlayer = null;
		// Datastructures init
		cities = new ArrayList<>();
		contractDeck = new LinkedList<>();
		longestPath = "";
		mostContracts = "";
		cities = new ArrayList<>();
		edges = new ArrayList<>();
		trainCardDeck = new Stack<>();
		displayCards = new ArrayList<>();
		players = new LinkedList<>();
		passedCities = new ArrayList<>();
		discardTrainCard = new ArrayList<>();
		lastRound = false;
		// Adding train cards
		for (int j = 0; j < 8; j++)
			for (int i = 0; i < 12; i++)
				trainCardDeck.add(new TrainCard(TRAIN_COLORS[j]));
		for (int i = 0; i < 14; i++)
			trainCardDeck.add(new TrainCard(TRAIN_COLORS[TRAIN_COLORS.length - 1]));
		for (int i = 0; i < 10; i++)
			Collections.shuffle(trainCardDeck);

		// Adding display cards
		for (int i = 0; i < 5; i++)
			displayCards.add(trainCardDeck.pop());

		// Adding players, cur player and turncounter
		for (int i = 0; i < PLAYER_COLORS.length; i++) {
			Player p = new Player(PLAYER_COLORS[i]);
			for (int j = 0; j < 4; j++)
				p.addTrainCard(trainCardDeck.pop());
			players.add(p);
		}
		curPlayer = players.poll();
		turnCounter = 2;

		// Edges and Cities
		Scanner scan = new Scanner(new File("TextFiles/Cities"));
		while (scan.hasNextLine()) {
			String[] temp1 = scan.nextLine().split(",");
			cities.add(new City(new Point(Integer.parseInt(temp1[1]), Integer.parseInt(temp1[2])), temp1[0],
					new ArrayList<Edge>()));
		}
		scan = new Scanner(new File("TextFiles/FixedCon"));
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			String[] tempFirstTwo = line.substring(0, line.indexOf('|')).split(",");
			String[] tempLastTwo = line.substring(line.indexOf('|') + 1, line.indexOf("/")).split(",");
			int serial = Integer.parseInt(line.substring(line.indexOf("/") + 1));
			ArrayList<City> tempCities = new ArrayList<>();
			String key = tempFirstTwo[0];
			String value = tempFirstTwo[1];
			tempCities.add(cityHelper(key));
			tempCities.add(cityHelper(value));
			edges.add(new Edge(Integer.parseInt(tempLastTwo[0]), tempLastTwo[1], tempCities, serial));
		}
		for (int i = cities.size() - 1; i >= 0; i--) {
			ArrayList<Edge> temps = new ArrayList<>();
			for (Edge e : edges) {
				if (e.getCities().contains(cities.get(i)))
					temps.add(e);
			}
			cities.add(new City(cities.get(i).getPoint(), cities.get(i).getName(), temps));
		}
		// adding contracts
		ArrayList<ContractCard> tempCards = new ArrayList<ContractCard>();
		scan = new Scanner(new File("TextFiles/tickets.txt"));
		int counter = Integer.parseInt(scan.nextLine());
		for (int i = 0; i < counter; i++) {
			String[] temp = scan.nextLine().split(",");
			City one = null, two = null;
			for (City c : cities) {
				if (c.getName().equals(temp[1]))
					one = c;
				if (c.getName().equals(temp[2]))
					two = c;
			}
			tempCards.add(new ContractCard(one, two, Integer.parseInt(temp[0]), i + 2));
		}
		Collections.shuffle(tempCards);
		for (int i = 0; i < tempCards.size(); i++)
			contractDeck.add(tempCards.get(i));
		scan.close();
	}

	public boolean placeTrain(Edge e, ArrayList<TrainCard> thing) { // player action
		if (choosingContracts || e.getHasTrains() || checkDoubleEdge(e) || turnCounter == 1)
			return false;
		ArrayList<TrainCard> input = new ArrayList<TrainCard>();
		input.addAll(thing);
		if (!e.getHasTrains() && input.size() == e.getLength()) {

			if (!e.getColor().equals("gray")) {
				for (int i = 0; i < input.size(); i++) {
					if (input.get(i).getColor().equals(e.getColor()) || input.get(i).getColor().equals("wild")) {
						input.remove(i);
						i--;
					}
				}
			} else {
				TrainCard tc = null;
				for (int i = 0; i < input.size(); i++) {
					if (!(input.get(i).getColor().equals("wild"))) {
						tc = input.remove(i);
						break;
					}
				}
				if (tc == null)
					tc = input.remove(0);
				for (int i = 0; i < input.size(); i++) {
					if (input.get(0).getColor().equals(tc.getColor()) || input.get(0).getColor().equals("wild")) {
						input.remove(i);
						i--;
					}
				}
			}
			if (!(input.size() > 0)) {
				setActualEdge(e, curPlayer.getTrainColor(), curPlayer);

				e.setPlayer(curPlayer);
				turnCounter -= 2;
				curPlayer.reduceTrains(e.getLength());
				curPlayer.addPoints(routePoints.get(e.getLength()));
				checkContracts();
				checkTurn();
				return true;
			}
		}
		return false;
	}

	public boolean chooseTrainCard(int choice) { // player action
		if (choosingContracts)
			return false;
		TrainCard t = displayCards.get(choice);
		if (t.getColor().equals("wild") && turnCounter == 2) {
			t = displayCards.remove(choice);
			curPlayer.addTrainCard(t);
			displayCards.add(choice, trainCardDeck.pop());
			turnCounter -= 2;
		} else if (!t.getColor().equals("wild")) {
			t = displayCards.remove(choice);
			curPlayer.addTrainCard(t);
			displayCards.add(choice, trainCardDeck.pop());
			turnCounter--;
		} else {
			// ystem.out.println("Stop right there, criminal scum!");
		}
		checkTurn();
		return true;
	}

	public void drawTrainCard() { // player action
		if (choosingContracts || trainCardDeck.isEmpty())
			return;
		curPlayer.addTrainCard(trainCardDeck.pop());
		turnCounter--;
		checkTurn();
	}

	public String chooseContractCard(ArrayList<Integer> choices, int amnt) { // player action
		if (!choosingContracts)
			return "";
		if (turnCounter == 1)
			return "";
		if (choices.size() < 1)
			return "Invalid. You must choose at least one contract.";
		for (int i = 0; i < amnt; i++)
			if (choices.contains(i))
				curPlayer.addContractCard(displayContracts.get(i));
		turnCounter -= 2;
		choosingContracts = false;
		displayContracts = null;
		checkTurn();
		return "Successful";
	}

	public boolean drawContracts(int amnt) {
		if (choosingContracts || turnCounter != 2)
			return false;
		choosingContracts = true;
		ArrayList<ContractCard> temps = new ArrayList<>();
		for (int i = 0; i < amnt; i++)
			temps.add(this.contractDeck.poll());
		displayContracts = temps;
		return true;
	}

	public void endGame() {
		isEnded = true;
		longestPath = longestPath();
		mostContracts = mostContractCards();

	}

	public void endTurn() {
		players.offer(curPlayer);
		curPlayer = players.poll();
		turnCounter = 2;
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

	public void checkTurn() {// does not continue to next round, due to the turn counter
		if (checkWilds()) { // if there are 3+ wild cards in the deck
			discardTrainCard.addAll(displayCards);
			displayCards.clear();
			Collections.shuffle(trainCardDeck);
			for (int i = 0; i < 5; i++)
				displayCards.add(trainCardDeck.pop());
		}
		if (lastRound && lastPlayer == curPlayer)
			endGame();
		if (curPlayer.getTrains() < 3 && !lastRound) {
			lastRound = true;
			lastPlayer = curPlayer;
		}

		if (turnCounter == 0 && !lastRound) {
			endTurn();
			return;
		}
	}

	public Player curPlayer() {
		return curPlayer;
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	public Queue<Player> getPlayerList() {
		return players;
	}

	public ArrayList<TrainCard> getDisplayCards() {
		return displayCards;
	}

	public ArrayList<ContractCard> getDisplayContracts() {
		return displayContracts;
	}

	public String getMostContracts() {
		return mostContracts;
	}

	public boolean isChoosingContracts() {
		return choosingContracts;
	}

	public boolean isEnded() {
		return isEnded;
	}

	public Queue<ContractCard> getContractDeck() {
		return contractDeck;
	}

	private boolean checkWilds() {
		int count = 0;
		for (TrainCard t : displayCards)
			if (t.getColor().equals("Rainbow"))
				count++;
		return count >= 3;
	}

	private City cityHelper(String name) {
		for (City c : cities)
			if (c.getName().equals(name))
				return c;
		return null;
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
			else {// there is a path, add points
				curPlayer.addPoints(c.getNumPoints());
				c.complete();
				// add method to make sure contracts are added to player, also setComplete
			}
		}

	}

	private ArrayList<Edge> checkContractsHelper(ArrayList<Edge> shared, ArrayList<Edge> cityEdges, City start) {
		if (start == null || cityEdges == null)
			return shared;
		else {
			Edge current = new Edge();
			for (Edge e : cityEdges) {
				if (e.getCities().contains(start)) {
					current = e;
					ArrayList<Edge> others = current.getOtherCity(start).getEdges(current.getTrainColor());
					others.remove(current);
					shared.add(current);
					if (!others.isEmpty())
						shared = checkContractsHelper(shared, others, current.getOtherCity(start));
					else
						return shared;
				}
			}
			return shared;
		}
	}

	private boolean checkDoubleEdge(Edge e) {
		for (int i = 0; i < edges.size(); i++) {
			ArrayList<City> tempCities = edges.get(i).getCities();
			if (edges.get(i).getHasTrains()) {
				if (tempCities.contains(e.getCities().get(0)) && tempCities.contains(e.getCities().get(1))) {
					if (edges.get(i).getTrainColor().equals(curPlayer.getTrainColor())
							&& e.getSerial() != edges.get(i).getSerial()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private void setActualEdge(Edge e, String s, Player p) {
		for (int i = 0; i < edges.size(); i++) {
			if (edges.get(i).compare(e)) {
				edges.get(i).setHasTrains(s);
				edges.get(i).setPlayer(p);
			}
		}
	}

	public ArrayList<String> endScreen()// returns the four players in order of points
	{
		ArrayList<String> list = new ArrayList<>();
		ArrayList<Player> winnerPoints = new ArrayList<>();
		winnerPoints.addAll(players);
		Collections.sort(winnerPoints, (p1, p2) -> Integer.compare(p1.getPoints(), p2.getPoints()));
		for (int i = 0; i < winnerPoints.size(); i++) {
			Player p = winnerPoints.get(i);
			list.add(i + ": " + p.getTrainColor() + ": " + p.getPoints());
		}

		return list;

	}
}
