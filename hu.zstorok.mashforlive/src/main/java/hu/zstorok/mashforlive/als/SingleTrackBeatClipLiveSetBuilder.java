package hu.zstorok.mashforlive.als;

import hu.zstorok.mashforlive.als.Clip.WarpMarker;
import hu.zstorok.mashforlive.client.EchoNestAnalysis;
import hu.zstorok.mashforlive.client.EchoNestAnalysis.Beat;

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
	public LiveSet build(EchoNestAnalysis echoNestAnalysis, String sampleFileName) {
		System.out.println(echoNestAnalysis);
		EchoNestAnalysis.Track track = echoNestAnalysis.getTrack();
		LiveSet liveSet = new LiveSet(track.getTempo());
		Track liveSetTrack = new Track(1, "track 1");
		List<Clip> liveSetTrackClips = liveSetTrack.getClips();
		List<Beat> beats = echoNestAnalysis.getBeats();
		for (int i = 0; i < beats.size(); i++) {
			Beat beat = beats.get(i);
			double clipStart = beat.getStart();
			double clipEnd = clipStart + beat.getDuration();
			liveSetTrackClips.add(new Clip(i, "clip " + i, sampleFileName, clipStart, clipEnd, buildWarpMarkers(echoNestAnalysis)));
		}
		liveSet.getTracks().add(liveSetTrack);
		return liveSet;
	}
	
	private List<WarpMarker> buildWarpMarkers(EchoNestAnalysis echoNestAnalysis) {
		return Collections.emptyList();
	}

}
