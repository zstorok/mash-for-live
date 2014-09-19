package hu.zstorok.mashforlive.als;

import java.util.Collections;
import java.util.List;

/**
 * POJO to represent a clip in an Ableton Live set.
 * 
 * @author zstorok
 */
public class LiveClip {

	private final int id;
	private final String name;
	private final double startPos;
	private final double endPos;
	private final List<WarpMarker> warpMarkers;
	private final String sampleFileName;
	private final ClipColor color;
	private final boolean repeat;

	public LiveClip() {
		this(-1, null, null, -1, -1, Collections.emptyList(), ClipColor.Orange,
				true);
	}
	
	public LiveClip(int id, String name, String sampleFileName, double startPos,
			double endPos, List<WarpMarker> warpMarkers) {
		this(id, name, sampleFileName, startPos, endPos, warpMarkers, ClipColor
				.random(), true);
	}

	public LiveClip(int id, String name, String sampleFileName, double startPos,
			double endPos, List<WarpMarker> warpMarkers, ClipColor color,
			boolean repeat) {
		this.id = id;
		this.name = name;
		this.sampleFileName = sampleFileName;
		this.startPos = startPos;
		this.endPos = endPos;
		this.warpMarkers = warpMarkers;
		this.color = color;
		this.repeat = repeat;
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

	public ClipColor getColor() {
		return color;
	}

	public boolean isRepeat() {
		return repeat;
	}

	public boolean isNotRepeat() {
		return !repeat;
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
				+ ", sampleFileName=" + sampleFileName + ", color=" + color + "]";
	}
	
	public static enum ClipColor {
		Red("12"), Green("16"), Yellow("38"), Orange("36");

		private String value;

		private ClipColor(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return value;
		}

		public static ClipColor random() {
			double r = Math.random();
			ClipColor c;
			if (r < 0.25) {
				c = ClipColor.Green;
			} else if (r < 0.5) {
				c = ClipColor.Orange;
			} else if (r < 0.75) {
				c = ClipColor.Red;
			} else {
				c = ClipColor.Yellow;
			}
			return c;
		}
	}
}
