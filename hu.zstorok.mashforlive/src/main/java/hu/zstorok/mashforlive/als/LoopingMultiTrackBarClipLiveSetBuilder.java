package hu.zstorok.mashforlive.als;

import hu.zstorok.mashforlive.als.LiveClip.ClipColor;
import hu.zstorok.mashforlive.als.LiveClip.WarpMarker;
import hu.zstorok.mashforlive.client.echonest.analyze.Analysis;
import hu.zstorok.mashforlive.client.echonest.analyze.Bar;
import hu.zstorok.mashforlive.client.echonest.analyze.Beat;
import hu.zstorok.mashforlive.client.echonest.analyze.Track;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * Ableton Live set builder implementation that distributes the clips created
 * from each bar of a song on multiple tracks.
 * 
 * @author zstorok
 */
public class LoopingMultiTrackBarClipLiveSetBuilder implements ILiveSetBuilder {

	private static final int NUMBER_OF_TRACKS = 8;

	@Override
	public LiveSet build(Analysis echoNestAnalysis, String sampleFileName) {
		Track track = echoNestAnalysis.getTrack();
		int timeSignature = track.getTimeSignature();
		LiveSet liveSet = new LiveSet(track.getTempo());
		List<Bar> bars = echoNestAnalysis.getBars();
		int barCount = bars.size();
		int clipsPerTrack = Math.floorDiv(barCount, NUMBER_OF_TRACKS) + 1;
		int trackIndex = 1;
		LiveTrack liveSetTrack = null;
		int i = 0;
		for (; i < barCount; i++) {
			if (i % clipsPerTrack == 0) {
				liveSetTrack = new LiveTrack(trackIndex, "track " + trackIndex);
				liveSet.getTracks().add(liveSetTrack);
				trackIndex++;
			}
			Bar bar = bars.get(i);
			List<WarpMarker> warpMarkers = buildWarpMarkers(echoNestAnalysis);
			// clip loop positions are in beats when the clip is warped
			liveSetTrack.getClips().add(new LiveClip(i, "clip " + i, sampleFileName, i * timeSignature, (i + 1) * timeSignature, warpMarkers, 
					ClipColor.random(), false));
		}
		// tracks with less clips need to be padded
		int emptyClipsToAdd = clipsPerTrack - i % clipsPerTrack;
		for (int j = 0; j < emptyClipsToAdd; j++) {
			liveSetTrack.getClips().add(new LiveClip());
		}
		return liveSet;
	}

	private List<WarpMarker> buildWarpMarkers(Analysis echoNestAnalysis) {
		List<Beat> beats = echoNestAnalysis.getBeats();
		ArrayList<WarpMarker> warpMarkers = Lists.newArrayList();
		for (int i = 0; i < beats.size(); i++) {
			Beat beat = beats.get(i);
			warpMarkers.add(new WarpMarker(beat.getStart(), i));
		}
		return warpMarkers;
	}

}