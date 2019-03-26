
import java.util.ArrayList;

public class Player {
	
	private int points; // points player has
	private ArrayList<ContractCard> contracts;
	private ArrayList<TrainCard> trainCards;
	private int trains; // number of trains
	private String trainColor; // color of player
	
	
	
	
	public Player(String trainColor) {
		super();
		this.points = 0;
		this.contracts = new ArrayList<ContractCard>();
		this.trainCards = new ArrayList<TrainCard>();
		this.trains = 40;
		this.trainColor = trainColor;
	}
	
	
	public int getPoints() {
		return points;
	}
	public void addPoints(int points) {
		this.points = points;
	}
	public ArrayList<ContractCard> getContracts() {
		return contracts;
	}
	public void setContracts(ArrayList<ContractCard> contracts) {
		this.contracts = contracts;
	}
	public ArrayList<TrainCard> getTrainCards() {
		return trainCards;
	}
	public void setTrainCards(ArrayList<TrainCard> trainCards) {
		this.trainCards = trainCards;
	}
	public int getTrains() {
		return trains;
	}
	public void reduceTrains(int trains) {
		this.trains -= trains;
	}
	public String getTrainColor() {
		return trainColor;
	}
	public void setTrainColor(String trainColor) {
		this.trainColor = trainColor;
	}
	
	
}
