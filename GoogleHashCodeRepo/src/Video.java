import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Video implements Comparable<Video> {
	public int len;
	public int iD;
	public List<Cache> caches;
	public Map<Endpoint, Integer> reqs; // endpoints to # of reqs.
	public int currScore;
	
	public Video(int len, int iD) {
		this.caches = new ArrayList<Cache>();
		this.len = len;
		this.iD = iD;
		this.reqs = new HashMap<Endpoint, Integer>();
	}
	
	public String toString() {
		String res = "Video ID: " + iD;
		res += " Size: " + len + "MB\n";
		res += "Endpoints and reqs: " + this.reqs;
		return res;
	}
	
	public Comparator<Cache> cacheComparator = new Comparator<Cache>() {
		@Override
		public int compare(Cache o1, Cache o2) {
			// First cache score
			int scoreOne = 0;
			int scoreTwo = 0;
			for (Endpoint key : reqs.keySet()) {
				if (key.cacheLatencies.containsKey(o1.iD)) {
					scoreOne += ((key.dataCenterLatency - key.cacheLatencies.get(o1.iD)) * reqs.get(key));
				}
				if (key.cacheLatencies.containsKey(o2.iD)) {
					scoreTwo += ((key.dataCenterLatency - key.cacheLatencies.get(o2.iD)) * reqs.get(key));
				}
			}
			return Integer.compare(scoreTwo, scoreOne);
		}
		
	};

	public void sortCaches(Map<Integer, Cache> cacheList) {
		for (int i = caches.size() - 1; i >= 0; i--) {
			if (caches.get(i).remaining < this.len) {
				caches.remove(i);
			}
		}
		if (this.caches.isEmpty()) {
			this.currScore = 0;
			return;
		}
		Collections.sort(this.caches, cacheComparator);
		Cache front = this.caches.get(0);
		int score = 0;
		for (Endpoint key : reqs.keySet()) {
			if (key.cacheLatencies.containsKey(front.iD)) {
				score += ((key.dataCenterLatency - key.cacheLatencies.get(front.iD)) * reqs.get(key));
			}
		}
		this.currScore = score;
	}
	
	public int compareTo(Video other) {
		return Integer.compare(other.currScore, this.currScore);
	}
}
