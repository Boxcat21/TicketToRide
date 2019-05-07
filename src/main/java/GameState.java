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
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeMap;

public class GameState {
	// i made this public, neccessary for graphics so the whole datastructure doesnt
	// need to be remade, can just refer to the index
	public static final String[] TRAIN_COLORS = {"Purple","White","Blue","Yellow","Orange","Black","Red","Green"};
	public static final String[] PLAYER_COLORS = {"Red","Green","Yellow","Blue"};
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
		isEnded = false;
		lastPlayer = null;
		//Datastructures init
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
		//Adding train cards
		for (int j = 0; j < 8; j++)
			for (int i = 0; i < 12; i++)
				trainCardDeck.add(new TrainCard(TRAIN_COLORS[j]));
		for (int i = 0; i < 14; i++)
			trainCardDeck.add(new TrainCard("Wild"));
		Collections.shuffle(trainCardDeck);

		//Adding display cards
		for (int i = 0; i < 5; i++)
			displayCards.add(trainCardDeck.pop());

		//Adding players, cur player and turncounter
		for (int i = 0; i < PLAYER_COLORS.length; i++) {
			Player p = new Player(PLAYER_COLORS[i]);
			for(int j = 0; j < 4; j++)
				p.addTrainCard(trainCardDeck.pop());
			players.add(p);
		}
		curPlayer = players.poll();
		turnCounter = 2;

		//Edges and Cities
		Scanner scan = new Scanner(new File("Cities"));
		while (scan.hasNextLine()) {
			String[] temp1 = scan.nextLine().split(",");
			cities.add(new City(new Point(Integer.parseInt(temp1[1]), Integer.parseInt(temp1[2])), temp1[0],
					new ArrayList<Edge>()));
		}
		scan = new Scanner(new File("FixedCon"));
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			String[] tempFirstTwo = line.substring(0, line.indexOf('|')).split(",");
			String[] tempLastTwo = line.substring(line.indexOf('|') + 1, line.length()).split(",");
			ArrayList<City> tempCities = new ArrayList<>();
			String key = tempFirstTwo[0];
			String value = tempFirstTwo[1];
			tempCities.add(cityHelper(key));
			tempCities.add(cityHelper(value));
			edges.add(new Edge(Integer.parseInt(tempLastTwo[0]), tempLastTwo[1], tempCities));
		}
		for (int i = cities.size() - 1; i >= 0; i--) {
			ArrayList<Edge> temps = new ArrayList<>();
			for (Edge e : edges) {
				if (e.getCities().contains(cities.get(i)))
					temps.add(e);
			}
			cities.add(new City(cities.get(i).getPoint(), cities.get(i).getName(), temps));
		}
		//adding contracts
		ArrayList<ContractCard> tempCards = new ArrayList<ContractCard>();
		scan = new Scanner(new File("tickets.txt"));
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
			tempCards.add(new ContractCard(one, two, Integer.parseInt(temp[0])));
		}
		Collections.shuffle(tempCards);
		for(int i = 0; i < tempCards.size(); i++)
			contractDeck.add(tempCards.get(i));
		scan.close();
	}

	public void placeTrain(Edge e, ArrayList<TrainCard> input) { //player action
		if(choosingContracts)
			return;
		if (!e.getHasTrains() && input.size() == e.getLength()) {
			
			for(int i = 0; i < input.size(); i++) {
				if(input.get(i).getColor().equals(e.getColor()) || input.get(i).getColor().equals("wild")) {
					input.remove(i);
					i--;
				}
			}
			if(!(input.size() > 0)) {
				e.setHasTrains();
				e.setPlayer(curPlayer);
				turnCounter -= 2;
				curPlayer.reduceTrains(e.getLength());
			}
		}
		checkContracts();
		checkTurn();
	}
	public boolean chooseTrainCard(int choice) { //player action
		if(choosingContracts)
			return false;
		TrainCard t = displayCards.get(choice);
		if (t.getColor().equals("Wild") && turnCounter == 2) {
			t = displayCards.remove(choice);
			curPlayer.addTrainCard(t);
			displayCards.add(trainCardDeck.pop());
			turnCounter -= 2;
		} else if (!t.getColor().equals("Wild")) {
			t = displayCards.remove(choice);
			curPlayer.addTrainCard(t);
			displayCards.add(trainCardDeck.pop());
			turnCounter--;
		} else {
			System.out.println("Stop right there, criminal scum!");
		}
		System.out.println(turnCounter);
		System.out.println(curPlayer.getTrainCards());
		checkTurn();
		return true;
	}

	public void drawTrainCard() { //player action
		if(choosingContracts)
			return;
		curPlayer.addTrainCard(trainCardDeck.pop());
		turnCounter--;
		checkTurn();
	}
	
	public String chooseContractCard(ArrayList<Integer> choices, int amnt) { //player action
		if(!choosingContracts)
			return "";
		if (choices.size() < 1)
			return "Invalid. You must choose at least one contract.";
		for (int i = 0; i < amnt; i++)
			if (choices.contains(i))
				curPlayer.addContractCard(displayContracts.get(i));
		System.out.println(curPlayer.getContracts());
		turnCounter -= 2;
		choosingContracts = false;
		
		checkTurn();
		return "Successful";
	}
	public ArrayList<ContractCard> drawContracts(int amnt) {
		choosingContracts = true;
		ArrayList<ContractCard> temps = new ArrayList<>();
		for (int i = 0; i < amnt; i++)
			temps.add(this.contractDeck.poll());
		displayContracts = temps;
		return temps;
	}
	public ArrayList<Player> endGame() {
		isEnded = true;
		longestPath = longestPath();
		mostContracts = mostContractCards();
		ArrayList<Player> winnerPoints = new ArrayList<>();
		winnerPoints.addAll(players);
		Collections.sort(winnerPoints, (p1, p2) -> Integer.compare(p1.getPoints(), p2.getPoints()));
		// sorts the winners by order of points
		return winnerPoints;
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
	public Player getCurPlayer() {return curPlayer;}
	
	public ArrayList<Edge> getEdges() {return edges;}
	
	public Queue<Player> getPlayerList() {return players;}
	
	public ArrayList<TrainCard> getDisplayCards() {return displayCards;}
	
	public String getMostContracts() {return mostContracts;}
	
	public boolean isEnded() {return isEnded;}
	
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
}
