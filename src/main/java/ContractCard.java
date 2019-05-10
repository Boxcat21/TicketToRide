
public class ContractCard {

	private boolean complete;
	private City[] cities = new City[2];
	private int value;
	private int count;
	
	public ContractCard(City one, City two, int p, int c)
	{
		cities[0] = one;
		cities[1] = two;
		value = p;
		complete = false;
		count = c;
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
	public int getSerial() {
		return count;
	}
	@Override
	public String toString() {
		return "|" + cities[0].getName() + " to " + cities[1].getName() + "|" + count;
	}
}
