import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
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
	
	public GameState() throws FileNotFoundException {
			//Reading in contracts
			cities = new ArrayList<>();
			contractList = new LinkedList<>();
			Scanner scan = new Scanner(new File("tickets.txt"));
			for(int i =0;i<Integer.parseInt(scan.nextLine());i++)
			{
				String[] temp = scan.nextLine().split("|");
				City one = null;
				City two = null;
				for(City c:cities)
				{
					if(c.getName().equals(temp[0]))
					one = c;
					if(c.getName().equals(temp[1]))
					two = c;		
				}
				contractList.add(new ContractCard(one,two,Integer.parseInt(temp[2])));			
			}
			//Adding train cards
			ArrayList<TrainCard> list = new ArrayList<TrainCard>();
			trainCardDeck = new Stack();
			for(int j=0;j<8;j++)
			for(int i=0;i<12;i++)
			{
				if(j==0)
					trainCardDeck.add(new TrainCard("Purple"));	
				else if(j==1)
					trainCardDeck.add(new TrainCard("White"));	
				else if(j==2)
					trainCardDeck.add(new TrainCard("Blue"));
				else if(j==3)
					trainCardDeck.add(new TrainCard("Yellow"));	
				else if(j==4)
					trainCardDeck.add(new TrainCard("Orange"));	
				else if(j==5)
					trainCardDeck.add(new TrainCard("Black"));	
				else if(j==6)
					trainCardDeck.add(new TrainCard("Red"));
				else if(j==7)
					trainCardDeck.add(new TrainCard("Green"));	
			}
			for(int i=0;i<14;i++)
				trainCardDeck.add(new TrainCard("Wild"));
			Collections.shuffle(trainCardDeck);
			//discard list
			discardTrainCard = new ArrayList<>();
			//display cards
			displayCards = new ArrayList<>();
			for(int i = 0;i<5;i++)
			{
				displayCards.add(trainCardDeck.pop());
			}

			//adding players, cur player and turncounter
			players = new LinkedList<>();
			players.add(new Player("red"));
			players.add(new Player("green"));
			players.add(new Player("yellow"));
			players.add(new Player("blue"));
			curPlayer = players.peek();
			turnCounter = 0;
			//edges and cities 		
			 scan = new Scanner(new File("ConnectedCities.txt"));
			 HashMap<String,String> previous = new HashMap<>();
			 
			 while(scan.hasNextLine())
			 {
				 String[] temp = scan.nextLine().split(",");
				 if(!((previous.get(temp[1]).equals(temp[0])||(previous.get(temp[0]).equals(temp[1]))))) {
					 
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
		turnCounter-=2;
		
	}
	public boolean chooseTrainCard(int choice) {
		TrainCard t = displayCards.remove(choice);
		
		if(t.getColor().equals("rainbow")) {
			turnCounter -= 2;
			if(turnCounter < 0)
				return false;
		}
		else
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
	public void endGame() {
		
	}
	public void endTurn() {
		
	}
	public String longestPath() {
		return "";
	}
	public String mostContractCards() {
		return "";
	}
	public void checkTurn() {
		if(this.turnCounter == 0)
			endTurn();
	}
}
