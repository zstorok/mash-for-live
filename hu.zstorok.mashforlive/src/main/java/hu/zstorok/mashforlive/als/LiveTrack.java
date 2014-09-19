package hu.zstorok.mashforlive.als;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * POJO to represent a track in an Ableton Live set.
 * 
 * @author zstorok
 */
public class LiveTrack {
	private final int id;
	private final String name;
	private final List<LiveClip> clips = Lists.newArrayList();

	public LiveTrack(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public List<LiveClip> getClips() {
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
