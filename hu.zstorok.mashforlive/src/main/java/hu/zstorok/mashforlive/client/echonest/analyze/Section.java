package hu.zstorok.mashforlive.client.echonest.analyze;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO to represent a section in the Echo Nest analysis results.
 * 
 * @author cfstras
 * @author zstorok
 */
public class Section extends MusicElement {
	private int key;
	@JsonProperty("key_confidence")
	private double keyConfidence;
	private double loudness;
	private int mode;
	@JsonProperty("mode_confidence")
	private double modeConfidence;
	private double tempo;
	@JsonProperty("tempo_confidence")
	private double tempoConfidence;
	@JsonProperty("time_signature")
	private int timeSignature;
	@JsonProperty("time_signature_confidence")
	private double timeSignatureConfidence;

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public double getKeyConfidence() {
		return keyConfidence;
	}

	public void setKeyConfidence(double keyConfidence) {
		this.keyConfidence = keyConfidence;
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

	public double getModeConfidence() {
		return modeConfidence;
	}

	public void setModeConfidence(double modeConfidence) {
		this.modeConfidence = modeConfidence;
	}

	public double getTempo() {
		return tempo;
	}

	public void setTempo(double tempo) {
		this.tempo = tempo;
	}

	public int getTimeSignature() {
		return timeSignature;
	}

	public void setTimeSignature(int timeSignature) {
		this.timeSignature = timeSignature;
	}

	public double getTimeSignatureConfidence() {
		return timeSignatureConfidence;
	}

	public void setTimeSignatureConfidence(double timeSignatureConfidence) {
		this.timeSignatureConfidence = timeSignatureConfidence;
	}

}