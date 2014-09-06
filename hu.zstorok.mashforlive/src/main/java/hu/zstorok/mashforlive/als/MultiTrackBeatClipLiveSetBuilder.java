package hu.zstorok.mashforlive.als;

import hu.zstorok.mashforlive.als.Clip.WarpMarker;
import hu.zstorok.mashforlive.client.EchoNestAnalysis;
import hu.zstorok.mashforlive.client.EchoNestAnalysis.Beat;

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
	public LiveSet build(EchoNestAnalysis echoNestAnalysis, String sampleFileName) {
		EchoNestAnalysis.Track track = echoNestAnalysis.getTrack();
		LiveSet liveSet = new LiveSet(track.getTempo());
		List<Beat> beats = echoNestAnalysis.getBeats();
		int beatCount = beats.size();
		int clipsPerTrack = Math.floorDiv(beatCount, NUMBER_OF_TRACKS) + 1;
		int trackIndex = 1;
		Track liveSetTrack = null;
		int i = 0;
		for (; i < beatCount; i++) {
			if (i % clipsPerTrack == 0) {
				liveSetTrack = new Track(trackIndex, "track " + trackIndex);
				liveSet.getTracks().add(liveSetTrack);
				trackIndex++;
			}
			Beat beat = beats.get(i);
			List<WarpMarker> warpMarkers = buildWarpMarkers(echoNestAnalysis);
			double clipStart = beat.getStart();
			double clipEnd = clipStart + beat.getDuration();
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
