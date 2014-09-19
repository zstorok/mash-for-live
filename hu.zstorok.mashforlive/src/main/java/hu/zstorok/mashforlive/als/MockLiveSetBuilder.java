package hu.zstorok.mashforlive.als;

import hu.zstorok.mashforlive.als.LiveClip.WarpMarker;
import hu.zstorok.mashforlive.client.echonest.analyze.Analysis;

import java.util.Arrays;

/**
 * Mock Ableton Live set builder implementation.
 * 
 * @author zstorok
 */
public class MockLiveSetBuilder implements ILiveSetBuilder {

	@Override
	public LiveSet build(Analysis echoNestAnalysis, String sampleFileName) {
		LiveTrack track1 = new LiveTrack(1, "track1");
		track1.getClips().add(new LiveClip(1, "clip1", sampleFileName, 1, 2, Arrays.asList(new WarpMarker(0, 1))));
		track1.getClips().add(new LiveClip(2, "clip2", sampleFileName, 3, 4, Arrays.asList()));
		LiveSet liveSet = new LiveSet(120);
		liveSet.getTracks().add(track1);
		return liveSet;
	}

}
