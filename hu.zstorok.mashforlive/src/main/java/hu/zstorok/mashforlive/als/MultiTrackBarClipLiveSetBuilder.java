package hu.zstorok.mashforlive.als;

import hu.zstorok.mashforlive.als.Clip.WarpMarker;
import hu.zstorok.mashforlive.client.EchoNestAnalysis;
import hu.zstorok.mashforlive.client.EchoNestAnalysis.Bar;

import java.util.Collections;
import java.util.List;

/**
 * Ableton Live set builder implementation that distributes the clips created
 * from each bar of a song on multiple tracks.
 * 
 * @author zstorok
 */
public class MultiTrackBarClipLiveSetBuilder implements ILiveSetBuilder {

	private static final int NUMBER_OF_TRACKS = 8;

	@Override
	public LiveSet build(EchoNestAnalysis echoNestAnalysis, String sampleFileName) {
		EchoNestAnalysis.Track track = echoNestAnalysis.getTrack();
		LiveSet liveSet = new LiveSet(track.getTempo());
		List<Bar> bars = echoNestAnalysis.getBars();
		int barCount = bars.size();
		int clipsPerTrack = Math.floorDiv(barCount, NUMBER_OF_TRACKS) + 1;
		int trackIndex = 1;
		Track liveSetTrack = null;
		int i = 0;
		for (; i < barCount; i++) {
			if (i % clipsPerTrack == 0) {
				liveSetTrack = new Track(trackIndex, "track " + trackIndex);
				liveSet.getTracks().add(liveSetTrack);
				trackIndex++;
			}
			Bar bar = bars.get(i);
			List<WarpMarker> warpMarkers = buildWarpMarkers(echoNestAnalysis);
			double clipStart = bar.getStart();
			double clipEnd = clipStart + bar.getDuration();
			liveSetTrack.getClips().add(new Clip(i, "clip " + i, sampleFileName, clipStart, clipEnd, warpMarkers));
		}
		// tracks with less clips need to be padded
		int emptyClipsToAdd = clipsPerTrack - i % clipsPerTrack;
		for (int j = 0; j < emptyClipsToAdd; j++) {
			liveSetTrack.getClips().add(new Clip());
		}
		return liveSet;
	}

	private List<WarpMarker> buildWarpMarkers(EchoNestAnalysis echoNestAnalysis) {
		return Collections.emptyList();
	}

}
