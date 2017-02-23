import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
	public static int HEIGHT;
	public static int WIDTH;
	public static int L;
	public static int H;
	public static final String FILENAME = "kittens.in";
	public static final String OUT = "kittens.out";

	
	public static void main(String[] args) throws FileNotFoundException {
		int[] stats = new int[5];
		Map<Integer, Video> vids = Parser.parseFile(FILENAME, stats);
		Map<Integer, Cache> cache = new HashMap<Integer, Cache>();
		for (int i = 0; i < stats[3]; i++) {
			cache.put(i, new Cache(i));
		}
		while (!setVideos(cache, vids)) {
			System.out.println(vids.size() + " videos remaining.");
		}
		
		PrintStream out = new PrintStream(new File(OUT));
		List<Cache> finalList = new ArrayList<Cache>();
		for (Integer key : cache.keySet()) {
			Cache next = cache.get(key);
			if (!next.videos.isEmpty()) {
				finalList.add(next);
			}
		}
		out.println(finalList.size());
		for (Cache c : finalList) {
			out.println(c);
		}
	}
	
	public static boolean setVideos(Map<Integer, Cache> caches, Map<Integer, Video> vids) {
		List<Integer> emptyVids = new ArrayList<Integer>();
		for (Integer vidID : vids.keySet()) {
			Video vid = vids.get(vidID);
			for (Endpoint end : vid.reqs.keySet()) {
				for (Integer id : end.cacheLatencies.keySet()) {
					if (!vid.caches.contains(caches.get(id)))
						vid.caches.add(caches.get(id));
				}
				if (vid.caches.isEmpty()) {
					emptyVids.add(vid.iD);
				} else {
					vid.sortCaches(caches);
				}
			}
		}
		for (Integer id : emptyVids) {
			vids.remove(id);
		}
		List<Video> videos = new ArrayList<Video>();
		for (Integer key : vids.keySet()) {
			Video entry = vids.get(key);
			if (entry.caches.size() > 0) {
				videos.add(entry);
			}
		}
		if (videos.isEmpty()) {
			return true;
		}
		Collections.sort(videos);
		Video toAssign = videos.remove(0);
		Cache toAllocate = toAssign.caches.get(0);
		Cache toUpdate = caches.get(toAllocate.iD);
		toUpdate.addVideo(toAssign);
		vids.clear();
		for (Video vid : videos) {
			if (vid.caches.contains(toUpdate)) {
				vid.caches.get(vid.caches.indexOf(toUpdate)).remaining = toUpdate.remaining;
			}
			vids.put(vid.iD, vid);
		}
		return false;
	}
}

