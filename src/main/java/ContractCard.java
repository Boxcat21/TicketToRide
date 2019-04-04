
public class ContractCard {

	private boolean complete;
	private City[] cities = new City[2];
	private int value;
	
	public ContractCard(City one, City two, int p)
	{
		cities[0] = one;
		cities[1] = two;
		value = p;
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
	public City getCity1() {
		return cities[0];
	}
	public City getCity2() {
		return cities[1];
	}
}
