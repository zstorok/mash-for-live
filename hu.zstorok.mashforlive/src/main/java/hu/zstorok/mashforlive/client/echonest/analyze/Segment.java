package hu.zstorok.mashforlive.client.echonest.analyze;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO to represent a segment in the Echo Nest analysis results.
 * 
 * @author cfstras
 * @author zstorok
 */
public class Segment extends MusicElement {

	@JsonProperty("loudness_start")
	private double loudnessStart;
	@JsonProperty("loudness_end")
	private double loudnessEnd;
	@JsonProperty("loudness_max")
	private double loudnessMax;
	@JsonProperty("loudness_max_time")
	private double loudnessMaxTime;
	private double[] pitches;
	private double[] timbre;

	public double getLoudnessEnd() {
		return loudnessEnd;
	}

	public void setLoudnessEnd(double loudnessEnd) {
		this.loudnessEnd = loudnessEnd;
	}

	public double getLoudnessStart() {
		return loudnessStart;
	}

	public void setLoudnessStart(double loudnessStart) {
		this.loudnessStart = loudnessStart;
	}

	public double getLoudnessMax() {
		return loudnessMax;
	}

	public void setLoudnessMax(double loudnessMax) {
		this.loudnessMax = loudnessMax;
	}

	public double getLoudnessMaxTime() {
		return loudnessMaxTime;
	}

	public void setLoudnessMaxTime(double loudnessMaxTime) {
		this.loudnessMaxTime = loudnessMaxTime;
	}

	public double[] getPitches() {
		return pitches;
	}

	public void setPitches(double[] pitches) {
		this.pitches = pitches;
	}

	public double[] getTimbre() {
		return timbre;
	}

	public void setTimbre(double[] timbre) {
		this.timbre = timbre;
	}

}