import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Parser {
	public static Map<Integer, Video> parseFile(String filename, int[] info) throws FileNotFoundException {
		Scanner file = new Scanner(new File(filename));
		String line = file.nextLine();
		Scanner tokens = new Scanner(line);
		
		// Get the initial specs of the file.
		info[0] = tokens.nextInt(); // V
		info[1] = tokens.nextInt(); // E
		info[2] = tokens.nextInt(); // R
		info[3] = tokens.nextInt(); // C
		info[4] = tokens.nextInt(); // X
		
		Cache.CAP = info[4];
		
		// Get the sizes of the vids.
		Map<Integer, Video> vids = new HashMap<Integer, Video>();
		List<Endpoint> ends = new ArrayList<Endpoint>();
		line = file.nextLine();
		tokens = new Scanner(line);
		for (int i = 0; i < info[0]; i++) {
			int size = tokens.nextInt();
			if (size <= Cache.CAP)
				vids.put(i, new Video(size, i));
		}
		
		// Get the latencies of the endpoints and cache connectivity.
		for (int i = 0; i < info[1]; i++) {
			line = file.nextLine();
			tokens = new Scanner(line);
			Endpoint next = new Endpoint(tokens.nextInt(), i);
			int K = tokens.nextInt();
			for (int j = 0; j < K; j++) {
				line = file.nextLine();
				tokens = new Scanner(line);
				next.cacheLatencies.put(tokens.nextInt(), tokens.nextInt());
			}
			ends.add(next);
		}
		
		for (int i = 0; i < info[2]; i++) {
			line = file.nextLine();
			tokens = new Scanner(line);
			int nextId = tokens.nextInt();
			if (!vids.containsKey(nextId))
				continue;
			Video vid = vids.get(nextId);
			Endpoint end = ends.get(tokens.nextInt());
			int reqs = tokens.nextInt();
			vid.reqs.put(end, reqs);
		}
		return vids;
	}
}
