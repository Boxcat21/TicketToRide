import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
			//Writing traincards
			ArrayList<TrainCard> list = new ArrayList<TrainCard>();
			trainCardDeck = new Stack();
			for(int i=0;i<4;i++)
			{
				//for(int i)
					
			}
			
			
					
				
			discardTrainCard = new ArrayList<>();
			//adding players
			players = new LinkedList<>();
			players.add(new Player("red"));
			players.add(new Player("green"));
			players.add(new Player("yellow"));
			players.add(new Player("blue"));

		
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
	public void chooseTrainCard(int choice) {
		TrainCard t = displayCards.remove(choice);
		
		if(t.getColor().equals("rainbow")) {
			turnCounter -= 2;
			if(turnCounter < 0) {
				turnCounter +=2;
				return;
			}
		}
		else
			turnCounter--;
		
		curPlayer.addTrainCard(t);
		
		checkTurn();
	}
	public void drawTrainCard() {
		turnCounter -= 2;
		if(turnCounter < 0) {
			turnCounter += 2;
			return;
		}
		else
			turnCounter--;
		
		curPlayer.addTrainCard(trainCardDeck.pop());
		
		checkTurn();
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
