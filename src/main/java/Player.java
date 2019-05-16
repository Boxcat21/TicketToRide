
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Player {
	
	private int points; // points player has
	private ArrayList<ContractCard> contracts;
	private ArrayList<TrainCard> trainCards;
	private int trains; // number of trains
	private String trainColor; // color of player
	private boolean isSetUp;
	private int longestPath;
	public ArrayList<Edge> playerEdges;
	public ArrayList<Set<City>> paths;

	public Player(String trainColor) {
		super();
		this.points = 0;
		this.contracts = new ArrayList<ContractCard>();
		this.trainCards = new ArrayList<TrainCard>();
		this.trains = 40;
		this.trainColor = trainColor;
		this.isSetUp = false;
		this.longestPath = 0;
		playerEdges = new ArrayList<>();
		paths = new ArrayList<Set<City>>();
	}
	
	public void addEdge(Edge e) {
		playerEdges.add(e);
		Set<City> set = new HashSet<>();
		if (paths.isEmpty()) {
			set.addAll(e.getCities());
			
			paths.add(set);
		}
		
		for (int i = 0; i < paths.size(); i++) {// contains 1
			Set<City> s = paths.get(i);
				if (s.contains(e.getCities().get(0)) && !exists(e.getCities().get(1)))
					s.add(e.getCities().get(1));
				else if (!exists(e.getCities().get(0)) && s.contains(e.getCities().get(1)))
					s.add(e.getCities().get(0));
				else if ((exists(e.getCities().get(0)) && exists(e.getCities().get(1)))) {
					Set<City> set1 = paths.get((getSetIndex(e.getCities().get(0))));
					Set<City> set2 = paths.get((getSetIndex(e.getCities().get(1))));
					set1.addAll(set2);
					if ( paths.get((getSetIndex(e.getCities().get(0)))) != paths.get((getSetIndex(e.getCities().get(1)))))
						paths.remove(set2);
				} else if (!exists(e.getCities().get(1)) && !exists(e.getCities().get(0))) {
					set.clear();
					set.addAll(e.getCities());
					paths.add(set);
				}

			}
		
	}
	
	private int getSetIndex(City c) {
		for ( int i = 0; i < paths.size(); i++) {
			Set<City> s  = paths.get(i);
			if ( s.contains(c))
				return i;
		}
		return -1;
		
	}
	
	private boolean exists(City c) {
		for ( Set<City> s : paths) 
			if (s.contains(c))
				return true;
		return false;
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
	public void addPath(int x) {
		if(x > longestPath)
			longestPath = x;
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
	public int getLongest() {
		return longestPath;
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
