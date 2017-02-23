import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Video {
	public int len;
	public int iD;
	public List<Endpoint> endpoints;
	
	public Video(int len, int iD) {
		this.endpoints = new ArrayList<Endpoint>();
		this.len = len;
		this.iD = iD;
	}
	
	public int compareTo(Video other) {
		
	}
}
