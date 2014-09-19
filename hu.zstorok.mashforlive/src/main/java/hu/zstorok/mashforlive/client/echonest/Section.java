package hu.zstorok.mashforlive.client.echonest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Section extends MusicElement {
	private int key;
	private double key_confidence;
	private double loudness;
	private int mode;
	private double mode_confidence;
	private double tempo;
	private int time_signature;
	private double time_signature_confidence;

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public double getKey_confidence() {
		return key_confidence;
	}

	public void setKey_confidence(double key_confidence) {
		this.key_confidence = key_confidence;
	}

	public double getLoudness() {
		return loudness;
	}

	public void setLoudness(double loudness) {
		this.loudness = loudness;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public double getMode_confidence() {
		return mode_confidence;
	}

	public void setMode_confidence(double mode_confidence) {
		this.mode_confidence = mode_confidence;
	}

	public double getTempo() {
		return tempo;
	}

	public void setTempo(double tempo) {
		this.tempo = tempo;
	}

	public int getTime_signature() {
		return time_signature;
	}

	public void setTime_signature(int time_signature) {
		this.time_signature = time_signature;
	}

	public double getTime_signature_confidence() {
		return time_signature_confidence;
	}

	public void setTime_signature_confidence(
			double time_signature_confidence) {
		this.time_signature_confidence = time_signature_confidence;
	}
}