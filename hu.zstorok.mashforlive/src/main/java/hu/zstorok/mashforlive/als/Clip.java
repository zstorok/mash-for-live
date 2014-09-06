package hu.zstorok.mashforlive.als;

import java.util.Collections;
import java.util.List;

/**
 * POJO to represent a clip in an Ableton Live set.
 * 
 * @author zstorok
 */
public class Clip {

	private final int id;
	private final String name;
	private final double startPos;
	private final double endPos;
	private final List<WarpMarker> warpMarkers;
	private final String sampleFileName;

	public Clip() {
		this(-1, null, null, -1, -1, Collections.emptyList());
	}
	
	public Clip(int id, String name, String sampleFileName, double startPos,
			double endPos, List<WarpMarker> warpMarkers) {
		this.id = id;
		this.name = name;
		this.sampleFileName = sampleFileName;
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
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getSampleFileName() {
		return sampleFileName;
	}
	
	public double getStartPos() {
		return startPos;
	}
	
	public double getEndPos() {
		return endPos;
	}

	public static class WarpMarker {
		private final double secTime;
		private final int beatTime;
		
		public WarpMarker(double secTime, int beatTime) {
			this.secTime = secTime;
			this.beatTime = beatTime;
		}

		public double getSecTime() {
			return secTime;
		}

		public int getBeatTime() {
			return beatTime;
		}
	}

	public boolean isEmpty() {
		return id == -1;
	}
	
	@Override
	public String toString() {
		return "Clip [id=" + id + ", name=" + name + ", startPos=" + startPos + ", endPos=" + endPos
				+ ", sampleFileName=" + sampleFileName + "]";
	}
	
}
