package hu.zstorok.mashforlive.als;

import hu.zstorok.mashforlive.als.LiveClip.WarpMarker;
import hu.zstorok.mashforlive.client.echonest.analyze.Analysis;
import hu.zstorok.mashforlive.client.echonest.analyze.Beat;
import hu.zstorok.mashforlive.client.echonest.analyze.Track;

import java.util.Collections;
import java.util.List;

/**
 * Ableton Live set builder implementation that creates a clip for each beat of
 * a song on a single track.
 * 
 * @author zstorok
 */
public class SingleTrackBeatClipLiveSetBuilder implements ILiveSetBuilder {

	@Override
	public LiveSet build(Analysis echoNestAnalysis, String sampleFileName) {
		Track track = echoNestAnalysis.getTrack();
		LiveSet liveSet = new LiveSet(track.getTempo());
		LiveTrack liveTrack = new LiveTrack(1, "track 1");
		List<LiveClip> liveTrackClips = liveTrack.getClips();
		List<Beat> beats = echoNestAnalysis.getBeats();
		for (int i = 0; i < beats.size(); i++) {
			Beat beat = beats.get(i);
			double clipStart = beat.getStart();
			double clipEnd = clipStart + beat.getDuration();
			liveTrackClips.add(new LiveClip(i, "clip " + i, sampleFileName, clipStart, clipEnd, buildWarpMarkers(echoNestAnalysis)));
		}
		liveSet.getTracks().add(liveTrack);
		return liveSet;
	}
	
	private List<WarpMarker> buildWarpMarkers(Analysis echoNestAnalysis) {
		return Collections.emptyList();
	}

}
