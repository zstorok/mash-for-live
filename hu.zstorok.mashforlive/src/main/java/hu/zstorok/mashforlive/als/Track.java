package hu.zstorok.mashforlive.als;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * POJO to represent a track in an Ableton Live set.
 * 
 * @author zstorok
 */
public class Track {
	private final String name;
	private final List<Clip> clips = Lists.newArrayList();

	public Track(String name) {
		this.name = name;
	}
	
	public List<Clip> getClips() {
		return clips;
	}
	
	public String getName() {
		return name;
	}
}
