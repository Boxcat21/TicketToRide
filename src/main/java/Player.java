
import java.util.ArrayList;

public class Player {
	
	private int points; // points player has
	private ArrayList<ContractCard> contracts;
	private ArrayList<TrainCard> trainCards;
	private int trains; // number of trains
	private String trainColor; // color of player
	private boolean isSetUp;

	public Player(String trainColor) {
		super();
		this.points = 0;
		this.contracts = new ArrayList<ContractCard>();
		this.trainCards = new ArrayList<TrainCard>();
		this.trains = 40;
		this.trainColor = trainColor;
		this.isSetUp = false;
	}
	
	
	public int getPoints() {
		return points;
	}
	public void addPoints(int points) {
		this.points += points;
	}
	public ArrayList<ContractCard> getContracts() {
		return contracts;
	}
	
	public int getCompleted()
	{
		ArrayList<ContractCard> temp = new ArrayList<ContractCard>();
		for(int i = 0; i < contracts.size(); i++)
		{
			if(contracts.get(i).isComplete())
			{
				temp.add(contracts.get(i));
			}
		}
		if(temp == null)
			return 0;
		return temp.size();
	}
	
	public ArrayList<TrainCard> getTrainCards() {
		return trainCards;
	}
	
	public int getTrains() {
		return trains;
	}
	
	public String getTrainColor() {
		return trainColor;
	}
	public void setTrainColor(String trainColor) {
		this.trainColor = trainColor;
	}
	
	public void addTrainCard(TrainCard t) {
		trainCards.add(t);
	}
	public void addContractCard(ContractCard c) {
		contracts.add(c);
	}
	public void addContractCards(ArrayList<ContractCard> list) {
		for(ContractCard c : list)
			contracts.add(c);
	}
	public boolean reduceTrains(int numRemoved) {
		if ( trains < 3) 
			return false;
		trains -= numRemoved;
		return true;
	}
	public int cardIndex(String s) {
		for(int i = 0; i < trainCards.size(); i++) {
			if(s.equals(trainCards.get(i).getColor()))
				return i;
		}
		return -1;
	}
	public String toString()
	{
		return "|" + trainColor + "|" + (points + 0.0) + "|#" + trains  + "|" + "\n" + this.trainCards + "\n" + this.contracts;
	}	
	public boolean isSetUp() {
		return isSetUp;
	}
	public void setUp() {
		isSetUp = true;
	}
}
