package hu.zstorok.mashforlive.als;

import hu.zstorok.mashforlive.als.LiveClip.WarpMarker;
import hu.zstorok.mashforlive.client.echonest.analyze.Analysis;
import hu.zstorok.mashforlive.client.echonest.analyze.Bar;
import hu.zstorok.mashforlive.client.echonest.analyze.Track;

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
	public LiveSet build(Analysis echoNestAnalysis, String sampleFileName) {
		Track track = echoNestAnalysis.getTrack();
		LiveSet liveSet = new LiveSet(track.getTempo());
		List<Bar> bars = echoNestAnalysis.getBars();
		int barCount = bars.size();
		int clipsPerTrack = Math.floorDiv(barCount, NUMBER_OF_TRACKS) + 1;
		int trackIndex = 1;
		LiveTrack liveTrack = null;
		int i = 0;
		for (; i < barCount; i++) {
			if (i % clipsPerTrack == 0) {
				liveTrack = new LiveTrack(trackIndex, "track " + trackIndex);
				liveSet.getTracks().add(liveTrack);
				trackIndex++;
			}
			Bar bar = bars.get(i);
			List<WarpMarker> warpMarkers = buildWarpMarkers(echoNestAnalysis);
			double clipStart = bar.getStart();
			double clipEnd = clipStart + bar.getDuration();
			liveTrack.getClips().add(new LiveClip(i, "clip " + i, sampleFileName, clipStart, clipEnd, warpMarkers));
		}
		// tracks with less clips need to be padded
		int emptyClipsToAdd = clipsPerTrack - i % clipsPerTrack;
		for (int j = 0; j < emptyClipsToAdd; j++) {
			liveTrack.getClips().add(new LiveClip());
		}
		return liveSet;
	}

	private List<WarpMarker> buildWarpMarkers(Analysis echoNestAnalysis) {
		return Collections.emptyList();
	}

}
