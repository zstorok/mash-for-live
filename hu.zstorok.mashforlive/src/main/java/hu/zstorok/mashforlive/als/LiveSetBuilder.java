package hu.zstorok.mashforlive.als;

import hu.zstorok.mashforlive.client.EchoNestAnalysis;

import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Ableton Live set builder implementation.
 * 
 * @author zstorok
 */
public class LiveSetBuilder implements ILiveSetBuilder {
	
	@Override
	public LiveSet build(EchoNestAnalysis echoNestAnalysis) {
		System.out.println(echoNestAnalysis);
		throw new UnsupportedOperationException("Not implemented.");
	}
	
	private void processSections(JsonNode analysis) {
		System.out.println("******** sections");
		JsonNode sections = analysis.get("sections");
		Iterator<JsonNode> elements = sections.elements();
		while (elements.hasNext()) {
			JsonNode section = (JsonNode) elements.next();
			String start = section.get("start").asText();
			String duration = section.get("duration").asText();
			double confidence = section.get("confidence").asDouble();
			System.out.println(start + ", " + duration + ", " + confidence);
		}
	}

	private void processBeats(JsonNode analysis) {
		System.out.println("******** beats");
		JsonNode beats = analysis.get("beats");
		Iterator<JsonNode> elements = beats.elements();
		int beatCounter = 0;
		String row = "<WarpMarker SecTime=\"" + "0.00000" + "\" BeatTime=\"" + beatCounter++ + "\" />";
		System.out.println(row);
		while (elements.hasNext()) {
			JsonNode beat = (JsonNode) elements.next();
			String time = beat.get("start").asText();
			row = "<WarpMarker SecTime=\"" + time + "\" BeatTime=\"" + beatCounter++ + "\" />";
			System.out.println(row);
		}
	}

	private void processBars(JsonNode analysis) {
		System.out.println("******** bars");
		JsonNode bars = analysis.get("bars");
		Iterator<JsonNode> elements = bars.elements();
		int barCounter = 0;
		String row = "<WarpMarker SecTime=\"" + "0.00000" + "\" BeatTime=\"" + barCounter++ + "\" />";
		System.out.println(row);
		while (elements.hasNext()) {
			JsonNode beat = (JsonNode) elements.next();
			String time = beat.get("start").asText();
			row = "<WarpMarker SecTime=\"" + time + "\" BeatTime=\"" + (4 * barCounter++) + "\" />";
			System.out.println(row);
		}
	}
}
