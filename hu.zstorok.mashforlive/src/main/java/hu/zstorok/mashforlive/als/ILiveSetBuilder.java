package hu.zstorok.mashforlive.als;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Interface for Ableton Live set builders.
 * 
 * @author zstorok
 */
public interface ILiveSetBuilder {

	/**
	 * Builds and returns an Ableton Live set model based on the EchoNest JSON data passed in.
	 * 
	 * @param echoNestAnalysis the EchoNest JSON data 
	 * @return the Ableton Live set model
	 */
	LiveSet build(JsonNode echoNestAnalysis);
}
