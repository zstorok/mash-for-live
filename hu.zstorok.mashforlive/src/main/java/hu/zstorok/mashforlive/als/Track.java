package hu.zstorok.mashforlive.als;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * POJO to represent a track in an Ableton Live set.
 * 
 * @author zstorok
 */
public class Track {
	private final int id;
	private final String name;
	private final List<Clip> clips = Lists.newArrayList();

	public Track(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public List<Clip> getClips() {
		return clips;
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Track [id=" + id + ", name=" + name + "]";
	}
	
}
