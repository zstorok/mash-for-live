package hu.zstorok.mashforlive.als;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * POJO to represent the Ableton Live set.
 * 
 * @author zstorok
 */
public class LiveSet {
	private final float tempo;
	private final List<Track> tracks = Lists.newArrayList();
	
	public LiveSet(float tempo) {
		this.tempo = tempo;
	}
	
	public List<Track> getTracks() {
		return tracks;
	}
	
	public float getTempo() {
		return tempo;
	}

	public List<String> getScenes() {
		List<String> scenes = Lists.newArrayList();
		int max=0;
		for (Track t : tracks) {
			int len = t.getClips().size();
			if (len > max) max = len;
		}
		for (int i=0; i<max; i++) {
			scenes.add("" + i);
		}
		return scenes;
	}
}
