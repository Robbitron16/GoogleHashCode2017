import java.util.HashMap;
import java.util.Map;

public class Endpoint {
	public Map<Integer, Integer> cacheLatencies; // cacheID, L_c.
	public int dataCenterLatency;
	public int iD;
	
	public Endpoint(int dataCenterLatency, int iD) {
		cacheLatencies = new HashMap<Integer, Integer>();
		this.dataCenterLatency = dataCenterLatency;
		this.iD = iD;
	}
	
	public String toString() {
		String res = "Endpoint " + this.iD + "DCL: " + this.dataCenterLatency + " CCLS: " + cacheLatencies;
		return res;
	}
}
