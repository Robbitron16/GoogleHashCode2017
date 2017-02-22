import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser {
	public static Cell[][] parseFile(String filename, int[] info) throws FileNotFoundException {
		Scanner file = new Scanner(new File(filename));
		String basicInfo = file.nextLine();
		Scanner tokens = new Scanner(basicInfo);
		
		// Get the initial specs of the pizza.
		info[0] = tokens.nextInt();
		info[1] = tokens.nextInt();
		info[2] = tokens.nextInt();
		info[3] = tokens.nextInt();
		
		// Initialize the pizza
		Cell[][] pizza = new Cell[info[0]][info[1]];
		int row = 0;
		while (file.hasNextLine()) {
			char[] line = file.nextLine().toCharArray();
			for (int i = 0; i < line.length; i++) {
				pizza[row][i] = new Cell(row, i, line[i] == 'M');
			}
			row++;
		}
		file.close();
		tokens.close();
		return pizza;
	}
}
