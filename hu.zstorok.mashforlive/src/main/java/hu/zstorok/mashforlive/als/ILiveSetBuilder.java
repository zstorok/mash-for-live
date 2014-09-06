package hu.zstorok.mashforlive.als;

import hu.zstorok.mashforlive.client.EchoNestAnalysis;

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
	 * @param sampleFileName the sample file name
	 * @return the Ableton Live set model
	 */
	LiveSet build(EchoNestAnalysis echoNestAnalysis, String sampleFileName);
}
