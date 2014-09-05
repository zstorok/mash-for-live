package hu.zstorok.mashforlive.als;

/**
 * POJO to represent a clip in an Ableton Live set.
 * 
 * @author zstorok
 */
public class Clip {

	private final String name;
	private final String startPos;
	private final String endPos;

	public Clip(String name, String startPos, String endPos) {
		this.name = name;
		this.startPos = startPos;
		this.endPos = endPos;
	}

	public String getName() {
		return name;
	}
	
	public String getStartPos() {
		return startPos;
	}
	
	public String getEndPos() {
		return endPos;
	}
}
