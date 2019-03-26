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
	
}
