package hu.zstorok.mashforlive.als;

import java.util.List;

/**
 * POJO to represent a clip in an Ableton Live set.
 * 
 * @author zstorok
 */
public class Clip {

	private final String name;
	private final String startPos;
	private final String endPos;
	private final List<WarpMarker> warpMarkers;

	public Clip(String name, String startPos, String endPos,
			List<WarpMarker> warpMarkers) {
		this.name = name;
		this.startPos = startPos;
		this.endPos = endPos;
		this.warpMarkers = warpMarkers;
	}

	public List<WarpMarker> getWarpMarkers() {
		return warpMarkers;
	}

	public boolean hasWarpMarkers() {
		return !warpMarkers.isEmpty();
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

	public static class WarpMarker {
		private final String secTime;
		private final String beatTime;

		public WarpMarker(String secTime, String beatTime) {
			this.secTime = secTime;
			this.beatTime = beatTime;
		}

		public String getSecTime() {
			return secTime;
		}

		public String getBeatTime() {
			return beatTime;
		}
	}
}
