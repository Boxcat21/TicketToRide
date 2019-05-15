import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class GameState {
	// i made this public, necessary for graphics so the whole data structure
	// doesn't
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
	private Player mostContracts;

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
		mostContracts = null;
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
			String[] tempFirstTwo = line.substring(0, line.indexOf('|')).split(",");//2 cities
			String[] tempLastTwo = line.substring(line.indexOf('|') + 1, line.indexOf("/")).split(",");//cnt and color
			int serial = Integer.parseInt(line.substring(line.indexOf("/") + 1));
			ArrayList<City> tempCities = new ArrayList<>();
			tempCities.add(cityHelper(tempFirstTwo[0]));
			tempCities.add(cityHelper(tempFirstTwo[1]));
			edges.add(new Edge(Integer.parseInt(tempLastTwo[0]), tempLastTwo[1], tempCities, serial));
		}
		for (int i = cities.size() - 1; i >= 0; i--) {
			ArrayList<Edge> temps = new ArrayList<>();
			for (Edge e : edges) {
				if (e.getCities().contains(cities.get(i)))
					temps.add(e);
			}
			City c = cities.remove(i);
			cities.add(new City(c.getPoint(), c.getName(), temps));

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
		boolean b = curPlayer.getTrains() < e.getLength();
		if (choosingContracts || e.getHasTrains() || checkDoubleEdge(e) || turnCounter == 1 || b)
			return false;
		ArrayList<TrainCard> input = new ArrayList<TrainCard>();
		ArrayList<TrainCard> tempDiscard = new ArrayList<>();
		input.addAll(thing);
		tempDiscard.addAll(input);
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
				discardTrainCard.addAll(tempDiscard);
				setActualEdge(e, curPlayer.getTrainColor(), curPlayer);
				e.setPlayer(curPlayer);
				curPlayer.addEdge(e);
				turnCounter -= 2;
				curPlayer.reduceTrains(e.getLength());
				curPlayer.addPoints(routePoints.get(e.getLength()));
				checkContracts();
				curPlayer.addPath(longestPath(e));
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
			try {
				displayCards.add(choice, trainCardDeck.pop());
			} catch (EmptyStackException e) {

			}
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
		// longestPath = longestPath();
		ArrayList<Player> plyrs = new ArrayList<Player>();

		while (players.size() > 0)
			plyrs.add(players.poll());
		for (int i = 0; i < plyrs.size(); i++)
			players.add(plyrs.get(i));
		int biggest = 0;
		Player big = null;
		for (int i = 0; i < plyrs.size(); i++) {
			if (plyrs.get(i).getLongest() > biggest) {
				biggest = plyrs.get(i).getLongest();
				big = plyrs.get(i);
			}
		}
		longestPath = "LONGEST PATH: |" + big.getTrainColor();
		big.addPath(20);

		mostContracts = mostContractCards();
		mostContracts.addPoints(10);
		addContractPoints();
		checkPoints(plyrs);

	}

	private void checkPoints(ArrayList<Player> p) {
		for (int i = 0; i < p.size(); i++) {
			if (p.get(i).getPoints() < 0)
				p.get(i).addPoints(-1 * (p.get(i).getPoints()));
		}

	}

	public void endTurn() {
		players.offer(curPlayer);
		curPlayer = players.poll();
		turnCounter = 2;
	}

	public int longestPath(Edge e) {
		// Ultimately how longest path works is: i calculate the longest path after each
		// edge is placed (from placeTrain())
		// after that, the max is stored in its respective player
		// this is done to split up the runtime of the algorithm between each turn
		// at the end, all of the players longest paths are compared and determined

		ArrayList<ArrayList<Edge>> possible1 = new ArrayList<>(); // list of paths from one city
		ArrayList<ArrayList<Edge>> possible2 = new ArrayList<>();// list of paths from another
		ArrayList<Edge> passedEdges = new ArrayList<>();// passed edges (only edges cant be repeated)
		passedEdges.add(e);

		
		ArrayList<Edge> same = e.getConnectedPlayerEdges(0);//connected edges from one city (corresponds to possible1)
		ArrayList<Edge> same1 = e.getConnectedPlayerEdges(1);//connnected edges from the other city (corresponds to possible2)
		
		if(same.size() == 0 && same1.size() == 0) {//accounts for case that if an edge is stand alone
			return e.getLength();
		}
		for(int i = 0; i < same.size(); i++) {
			possible1.add(longestPathRecur(same.get(i),passedEdges)); //all the possibilities for one city
		}
		for (int i = 0; i < same.size(); i++) {
			possible2.add(longestPathRecur(same1.get(i), passedEdges));// all the possibilities for the other city
		}

		// the following nested for loop block of code description:
		// this is taking all the combinations of paths from the given edge, and finding
		// the combination that is the longest path
		// taking into account repeats
		ArrayList<ArrayList<Edge>> finalSets = new ArrayList<>();
		for (int i = 0; i < possible1.size(); i++) {
			for (int t = 0; t < possible2.size(); t++) {
				System.out.println(i + " " + t);
				ArrayList<Edge> temp = new ArrayList<Edge>();
				temp.addAll(possible1.get(i));
				temp.addAll(possible2.get(t));
				removeRepeats(temp);// repeats are removed
				finalSets.add(temp);// added to final sets
			}
		}
		ArrayList<Edge> finalSet = getLargestArray(finalSets);// the longest path of those is the final longest path
		// int longest = longestPathRecur(e, new ArrayList<Edge>());

		return actualSize(finalSet);// returned to a player
	}

	public ArrayList<Edge> longestPathRecur(Edge e, ArrayList<Edge> passedEdges) {
		// the reason that this method returns arraylist instead of an int, is to
		// account for repeats later on

		ArrayList<Edge> same = e.getConnectedPlayerEdges();
		// ArrayList<Integer> findMax = new ArrayList<Integer>();
		// arraylist of arraylist of edges
		ArrayList<ArrayList<Edge>> findMax = new ArrayList<>();// store all possible paths
		for (int i = 0; i < same.size(); i++) {
			if (passedEdges.contains(same.get(i))) { // if i have passed over the edge
				same.remove(i);
				i--;
			}
		}
		passedEdges.add(e);
		if (same.size() > 0) {// if there are more edges to recursively traverse through
			for (int i = 0; i < same.size(); i++) {
				findMax.add(longestPathRecur(same.get(i), passedEdges));
			}
		}
		if (findMax.size() > 0) { // basically if the above if statemnet has run
			ArrayList<Edge> biggest = getLargestArray(findMax);
			biggest.add(e);// finds the longest path from this edge
			return biggest;
		} else {
			ArrayList<Edge> temp = new ArrayList<Edge>(); // if there are no more connected edges of the same color,
															// this edge is the only one and must be returned
			// basically this is the end of all recursive calls
			temp.add(e);
			// return e.getLength();
			return temp;
		}
	}

	public Player mostContractCards() {
		// Cole (DONE, untested)
		ArrayList<Player> plyrs = new ArrayList<Player>();

		while (players.size() > 0)
			plyrs.add(players.poll());
		for (int i = 0; i < plyrs.size(); i++)
			players.add(plyrs.get(i));
		Player highest = plyrs.get(0);

		System.out.println(plyrs.size());
		for (int i = 0; i < plyrs.size(); i++) {
			Player p = plyrs.get(i);
			System.out.println(i);
			if (p.getCompleted() > highest.getCompleted()) {
				highest = p;
			}
		}
		return highest;
	}

	public void checkTurn() {// does not continue to next round, due to the turn counter
		// we need to force next turn if stuck halfway through a turn
		while (displayCards.size() < 5 || checkWilds()) { // if there are 3+ wild cards in the deck
			discardTrainCard.addAll(displayCards);
			displayCards.clear();
			
			if (trainCardDeck.empty()) //If the original deck is completely empty
			{
			
			//Count the number of cards that are not wild in the discard deck	
				int counter = 0;
			for (TrainCard c : discardTrainCard)
				if (!c.getColor().equals("wild"))
					counter++;
			//If the discard deck has less than three normal cards, it would enter an infinite loop because once it adds to trainCardDeck,
			//it would pull 3 wilds and 2 normals, discard them because it violates the rules, pull it out again,etc.
			//Therefore, end the game if the discardDeck has less than three normal cards
			if(counter<3)
			{
				endGame();
				break;
			}
			
			//Otherwise, if there are more than three normal cards in the discard add them back to the train card deck
			else
			{
				trainCardDeck.addAll(discardTrainCard);
				Collections.shuffle(trainCardDeck);
				discardTrainCard.clear();
			}
				
			}
			for (int i = 0; i < 5; i++)
				displayCards.add(trainCardDeck.pop());
			
			
		}
		if (lastRound && lastPlayer == curPlayer && turnCounter == 0) {
			players.add(curPlayer);
			endGame();
			return;
		}
		if (curPlayer.getTrains() < 3 && !lastRound) {
			lastRound = true;
			lastPlayer = curPlayer;
		}
		if (turnCounter == 0) {
			endTurn();
			return;
		}

	}

	public ArrayList<String> endScreen()// returns the four players in order of points
	{
		ArrayList<String> list = new ArrayList<>();
		ArrayList<Player> winnerPoints = new ArrayList<>();
		winnerPoints.addAll(players);
		winnerPoints.add(curPlayer);
		Collections.sort(winnerPoints, (p1, p2) -> Integer.compare(p1.getPoints(), p2.getPoints()));
		Collections.reverse(winnerPoints);
		for (int i = 0; i < winnerPoints.size(); i++) {
			System.out.println(i);
			Player p = winnerPoints.get(i);
			list.add(p.getTrainColor() + ": " + p.getPoints());
		}

		return list;

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

	public Player getMostContracts() {
		return mostContracts;
	}

	public boolean isChoosingContracts() {
		return choosingContracts;
	}

	public boolean isEnded() {
		return isEnded;
	}

	public String getLongestPath() {
		return longestPath;
	}

	public Queue<ContractCard> getContractDeck() {
		return contractDeck;
	}

	private boolean checkWilds() {
		int count = 0;
		for (TrainCard t : displayCards)
			if (t.getColor().equals("wild"))
				count++;
		return count >= 3;
	}

	private City cityHelper(String name) {
		for (City c : cities)
			if (c.getName().equals(name))
				return c;
		return null;
	}

//	private void checkContracts() {
//		System.out.println(1);
//		ArrayList<ContractCard> contracts = curPlayer.getContracts();
//		for (ContractCard c : contracts) { // gives points and makes contracts complete
//			City one = c.getCity1();
//			City two = c.getCity2(); // are the references correct?
//
//			ArrayList<Edge> city1Edges = one.getEdges(curPlayer.getTrainColor());
//			ArrayList<Edge> city2Edges = two.getEdges(curPlayer.getTrainColor());
//
//			if (city1Edges.isEmpty() || city2Edges.isEmpty()) // no path, nothing to find here!
//				return;
//
//			// calls traversals from each city, storing the added edges
//			ArrayList<Edge> sharedCity1 = new ArrayList<>();
//			sharedCity1 = checkContractsHelper(sharedCity1, city1Edges, one);
//
//			ArrayList<Edge> sharedCity2 = new ArrayList<>();
//			sharedCity2 = checkContractsHelper(sharedCity2, city2Edges, two);
//			System.out.println(sharedCity1);
//			System.out.println(sharedCity2);
//			// retainAll edges, if resulting set.isEmpty(), no path
//			sharedCity1.retainAll(sharedCity2);
//			System.out.println(sharedCity1);
//			if (sharedCity1.isEmpty())
//				return;
//			else {// there is a path
//				c.complete();
//				// add method to make sure contracts are added to player, also setComplete
//			}
//		}
//		System.out.println(curPlayer.getCompleted());
//	}

//	private ArrayList<Edge> checkContractsHelper(ArrayList<Edge> shared, ArrayList<Edge> cityEdges, City start) {
//		System.out.println("L");
//		if (start == null || cityEdges == null) {
//			System.out.println("LBig");
//			return shared;
//		}
//
//		else {
//			Edge current = new Edge();
//			for (Edge e : cityEdges) {
//				current = e;
//				ArrayList<Edge> others = current.getOtherCity(start).getEdges(current.getTrainColor());
//				System.out.println(current.getOtherCity(start).getAllEdges());
//				System.out.println(current);
//				
//				System.out.println("others" + others);
//				others.remove(current);
//				shared.add(current);
//				
//				if (!others.isEmpty()) {
//					shared = checkContractsHelper(shared, others, current.getOtherCity(start));
//					System.out.println("another one");
//				} else {
//					return shared;
//				}
//			}
//		}
//		return shared;
//
//	}

	public void checkContracts() {

		for (ContractCard c : curPlayer.getContracts()) {

			for (Set<City> s : curPlayer.paths) {

				if (checkSameSet(c.getCity1(), c.getCity2(), s)) {
					c.complete();

				}
			}
		}

	}

	private boolean checkSameSet(City a, City b, Set<City> s) {
		boolean ba = false;
		boolean bb = false;

		for (City c : s) {
			if (c.getName().equals(a.getName()))
				ba = true;
			if (c.getName().equals(b.getName()))
				bb = true;
		}
		return ba && bb;
	}

	private void addContractPoints() {
		ArrayList<Player> pla = new ArrayList<>();
		pla.addAll(players);
		pla.add(curPlayer);

		for (int i = 0; i < pla.size(); i++) {
			for (ContractCard c : pla.get(i).getContracts()) {
				if (!c.isComplete())
					pla.get(i).addPoints(-1 * (c.getNumPoints()));
				else if (c.isComplete())
					pla.get(i).addPoints(c.getNumPoints());
			}
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

	// longest path helper
	private ArrayList<Edge> getLargestArray(ArrayList<ArrayList<Edge>> mat) {// Path = array of edges, //array of paths
																				// is an array of an array of edges
		// that is essentially what ArrayList<ArrayList<Edge>> is

		// basically this is really getting the largest path out of the given
		// possibilities
		ArrayList<Edge> biggest = mat.get(0);
		for (int i = 1; i < mat.size(); i++) {
			if (actualSize(mat.get(i)) > actualSize(biggest))
				biggest = mat.get(i);
		}
		return biggest;
	}

	// longest path helper
	private int actualSize(ArrayList<Edge> list) { // this basically takes a path of edges and calculates the actual
													// length
		// by adding up all the lengths of the contained edges
		int sum = 0;
		for (int i = 0; i < list.size(); i++) {
			sum += list.get(i).getLength();
		}
		return sum;
	}

	private ArrayList<Edge> removeRepeats(ArrayList<Edge> list) { // I realized that whenever i call longest path from a
																	// single edge
		// it branches off in multiple directions. I thought i could add all the total
		// branches but the issue was that there would be repeats
		// i created this method to solve it and made Edge implement comparable
		// Still not sure if this actually solves it tho
		ArrayList<Edge> temps = new ArrayList<Edge>();
		temps.add(list.get(0));
		for (int i = 1; i < list.size(); i++) {
			if (temps.contains(list.get(i))) {
				list.remove(i);
				i--;
			} else {
				temps.add(list.get(i));
			}
		}
		return list;
	}
}
