package hu.zstorok.mashforlive.als;

import hu.zstorok.mashforlive.als.Clip.WarpMarker;
import hu.zstorok.mashforlive.client.EchoNestAnalysis;

import java.util.Arrays;

/**
 * Mock Ableton Live set builder implementation.
 * 
 * @author zstorok
 */
public class MockLiveSetBuilder implements ILiveSetBuilder {

	@Override
	public LiveSet build(EchoNestAnalysis echoNestAnalysis) {
		Track track1 = new Track(1, "track1");
		track1.getClips().add(new Clip(1, "clip1", 1, 2, Arrays.asList(new WarpMarker(0, 1))));
		track1.getClips().add(new Clip(2, "clip2", 3, 4, Arrays.asList()));
		LiveSet liveSet = new LiveSet(120);
		liveSet.getTracks().add(track1);
		return liveSet;
	}

}
