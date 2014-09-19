package hu.zstorok.mashforlive.als;

import hu.zstorok.mashforlive.als.LiveClip.WarpMarker;
import hu.zstorok.mashforlive.client.echonest.analyze.Analysis;
import hu.zstorok.mashforlive.client.echonest.analyze.Beat;
import hu.zstorok.mashforlive.client.echonest.analyze.Track;

import java.util.Collections;
import java.util.List;

/**
 * Ableton Live set builder implementation that distributes the clips created
 * from each beat of a song on multiple tracks.
 * 
 * @author zstorok
 */
public class MultiTrackBeatClipLiveSetBuilder implements ILiveSetBuilder {

	private static final int NUMBER_OF_TRACKS = 8;
	
	@Override
	public LiveSet build(Analysis echoNestAnalysis, String sampleFileName) {
		Track track = echoNestAnalysis.getTrack();
		LiveSet liveSet = new LiveSet(track.getTempo());
		List<Beat> beats = echoNestAnalysis.getBeats();
		int beatCount = beats.size();
		int clipsPerTrack = Math.floorDiv(beatCount, NUMBER_OF_TRACKS) + 1;
		int trackIndex = 1;
		LiveTrack liveTrack = null;
		int i = 0;
		for (; i < beatCount; i++) {
			if (i % clipsPerTrack == 0) {
				liveTrack = new LiveTrack(trackIndex, "track " + trackIndex);
				liveSet.getTracks().add(liveTrack);
				trackIndex++;
			}
			Beat beat = beats.get(i);
			List<WarpMarker> warpMarkers = buildWarpMarkers(echoNestAnalysis);
			double clipStart = beat.getStart();
			double clipEnd = clipStart + beat.getDuration();
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
