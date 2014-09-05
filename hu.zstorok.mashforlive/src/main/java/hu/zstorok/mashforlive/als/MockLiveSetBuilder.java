package hu.zstorok.mashforlive.als;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Mock Ableton Live set builder implementation.
 * 
 * @author zstorok
 */
public class MockLiveSetBuilder implements ILiveSetBuilder {

	@Override
	public LiveSet build(JsonNode echoNestAnalysis) {
		Track track1 = new Track("track1");
		track1.getClips().add(new Clip("clip1", "1", "2"));
		track1.getClips().add(new Clip("clip2", "3", "4"));
		LiveSet liveSet = new LiveSet(120);
		liveSet.getTracks().add(track1);
		return liveSet;
	}

}
