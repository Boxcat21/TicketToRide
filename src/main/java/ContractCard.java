
public class ContractCard {

	private boolean complete;
	private City[] cities = new City[2];
	private int value;
	
	public ContractCard(City one, City two, int p)
	{
		cities[0] = one;
		cities[1] = two;
		value = p;
		complete = false;
	}
	
	public boolean isComplete()
	{
		return complete;
	}
	
	public int getNumPoints()
	{
		return value;
	}
	
	public void complete()
	{
		complete = true;
	}
	
}
