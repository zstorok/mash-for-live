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
}
