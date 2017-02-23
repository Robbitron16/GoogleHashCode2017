import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {
	public static Cell[][] parseFile(String filename, int[] info) throws FileNotFoundException {
		Scanner file = new Scanner(new File(filename));
		String line = file.nextLine();
		Scanner tokens = new Scanner(line);
		
		// Get the initial specs of the file.
		info[0] = tokens.nextInt(); // V
		info[1] = tokens.nextInt(); // E
		info[2] = tokens.nextInt(); // R
		info[3] = tokens.nextInt(); // C
		info[4] = tokens.nextInt(); // X
		
		// Get the sizes of the vids.
		List<Integer> sizes = new ArrayList<Integer>();
		line = file.nextLine();
		tokens = new Scanner(line);
		while (tokens.hasNextInt()) {
			sizes.add(tokens.nextInt());
		}
		
		
		
	}
}
