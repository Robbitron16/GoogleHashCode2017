import java.util.HashMap;
import java.util.Map;

public class Endpoint {
	public Map<Integer, Integer> cacheLatencies; // cacheID, time saved.
	public int dataCenterLatency;
	
	public Endpoint(int dataCenterLatency) {
		cacheLatencies = new HashMap<Integer, Integer>();
		this.dataCenterLatency = dataCenterLatency;
	}
}
