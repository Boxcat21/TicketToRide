import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
public class TestGame {
	public static void main(String[] args) throws FileNotFoundException {
		Scanner sc = new Scanner(System.in);
		
		GameState g = new GameState();
		
		int cnt = 0;
		while(!g.isEnded()) {
			if(cnt < 3) {
				System.out.println("\n" + g.getCurPlayer());
				System.out.println("Choose your contract cards(Enter list indecies)::");
				System.out.println(g.drawContracts(5));
				String temps[] = sc.nextLine().split(" ");
				ArrayList<Integer> choices = new ArrayList<Integer>();
				for(int i = 0; i < temps.length; i++)
					choices.add(Integer.parseInt(temps[i]));
				System.out.println(g.chooseContractCard(choices, 5));
				cnt++;
			}
			else
				break;
		}
		while(!g.isEnded()) {
			System.out.println("\n" + g.getCurPlayer());
			System.out.println("What would you like to do? (placeTrains, drawContracts, chooseContracts,"
					+ " drawTrainCard, chooseTrainCard, )");
			String s = sc.nextLine();
			
			if(s.equals("placeTrains")) {
				System.out.println(g.getCurPlayer().getTrainCards());
				System.out.println("Choose the cards desired (indecies):: ");
				String temp[] = sc.nextLine().split(" ");
				ArrayList<Integer> choices = new ArrayList<Integer>();
				
				for(int i = 0; i < temp.length; i++)
					choices.add(Integer.parseInt(temp[i]));
			}
			else if(s.equals("drawContracts")) {
				
			}
			else if(s.equals("chooseContracts")) {
				
			}
			else if(s.equals("chooseTrainCard")) {
				
			}
			else if(s.equals("drawTrainCard")) {
				
			}
			else
				System.out.println("TRY AGAIN");
		}
	}
}
