import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FixTextFile {
	public static void main(String[] args) throws FileNotFoundException {
		/*
		Scanner sc = new Scanner(new File("ConnectedCities.txt"));
		
		String[] lines = new String[201];
		
		int cnt = 0;
		while(sc.hasNextLine()) {
			lines[cnt] = sc.nextLine();
			cnt++;
		}
		
		ArrayList<String> added = new ArrayList<String>();
		for(int i = 0; i < lines.length; i++) {
			String beg = lines[i].substring(0, lines[i].indexOf("|"));
			
			if(!(added.contains(beg)))
				System.out.println(lines[i]);
			
			String s = lines[i].substring(0,lines[i].indexOf("|"));
			s = s.substring(s.indexOf(",")+1) +"," + s.substring(0, s.indexOf(","));
			
			added.add(s);
		}
		*/
		Scanner sc = new Scanner(new File("FixedCon"));
		
		ArrayList<String> added = new ArrayList<String>();
		while(sc.hasNextLine()) {
			String s = sc.nextLine();
			if(!added.contains(s)) {
				System.out.println(s);
				added.add(s);
			}
		}
	}
}
