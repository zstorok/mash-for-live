package hu.zstorok.mashforlive.client.echonest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Segment extends MusicElement {
	private double loudness_max;
	private double loudness_max_time;
	private double[] pitches;
	private double[] timbre;

	public double getLoudness_max() {
		return loudness_max;
	}

	public void setLoudness_max(double loudness_max) {
		this.loudness_max = loudness_max;
	}

	public double getLoudness_max_time() {
		return loudness_max_time;
	}

	public void setLoudness_max_time(double loudness_max_time) {
		this.loudness_max_time = loudness_max_time;
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