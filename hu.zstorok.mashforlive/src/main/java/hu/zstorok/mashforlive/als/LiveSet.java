package hu.zstorok.mashforlive.als;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * POJO to represent the Ableton Live set.
 * 
 * @author zstorok
 */
public class LiveSet {
	private final double tempo;
	private final List<LiveTrack> tracks = Lists.newArrayList();
	
	public LiveSet(double tempo) {
		this.tempo = tempo;
	}
	
	public List<LiveTrack> getTracks() {
		return tracks;
	}
	
	public double getTempo() {
		return tempo;
	}

	public List<String> getScenes() {
		List<String> scenes = Lists.newArrayList();
		int max=0;
		for (LiveTrack t : tracks) {
			int len = t.getClips().size();
			if (len > max) max = len;
		}
		for (int i=0; i<max; i++) {
			scenes.add("" + i);
		}
		return scenes;
	}

	@Override
	public String toString() {
		return "LiveSet [tempo=" + tempo + ", tracks=" + tracks + "]";
	}
	
}
