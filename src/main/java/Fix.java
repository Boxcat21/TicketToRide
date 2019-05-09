import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Fix {
	public static void main(String[] args) throws FileNotFoundException {
		Scanner sc = new Scanner(new File("FixedCon"));
		int cnt = 2;
		while(sc.hasNextLine()) {
			System.out.println(sc.nextLine() + "/" + (cnt%2));
			cnt++;
		}
	}
}
