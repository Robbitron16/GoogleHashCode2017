import java.util.ArrayList;
import java.util.List;

public class Cache {
	public static int CAP;
	public int remaining;
	public List<Video> videos;
	public int iD;
	
	
	public Cache(int iD) {
		this.iD = iD;
		this.remaining = Cache.CAP;
		this.videos = new ArrayList<Video>();
	}
	
	public String toString() {
		String res = this.iD + "";
		for (Video vid : videos) {
			res += (" " + vid.iD);
		}
		return res;
		
	}
	
	public void addVideo(Video vid) {
		videos.add(vid);
		this.remaining -= vid.len;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Cache)) {
			return false;
		}
		Cache oth = (Cache)other;
		return this.iD == oth.iD;
	}
}
