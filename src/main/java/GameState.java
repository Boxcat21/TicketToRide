import java.util.ArrayList;
import java.util.Queue;
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
	
	public GameState() {
		
	}
	public Player getCurPlayer() {
		return curPlayer;
	}
	public Queue<Player> getPlayerList() {
		return players;
	}
	public void placeTrain(Edge e) {
		
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
