package hu.zstorok.mashforlive.client.echonest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Track {
	private double duration, end_of_fade_in, loudness, start_of_fade_out,
			tempo, tempo_confidence;
	private int key, time_signature;
	private double key_confidence, time_signature_confidence;
	private String rhythmstring, sample_md5, synchstring, codestring;

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public double getEnd_of_fade_in() {
		return end_of_fade_in;
	}

	public void setEnd_of_fade_in(double end_of_fade_in) {
		this.end_of_fade_in = end_of_fade_in;
	}

	public double getLoudness() {
		return loudness;
	}

	public void setLoudness(double loudness) {
		this.loudness = loudness;
	}

	public double getStart_of_fade_out() {
		return start_of_fade_out;
	}

	public void setStart_of_fade_out(double start_of_fade_out) {
		this.start_of_fade_out = start_of_fade_out;
	}

	public double getTempo() {
		return tempo;
	}

	public void setTempo(double tempo) {
		this.tempo = tempo;
	}

	public double getTempo_confidence() {
		return tempo_confidence;
	}

	public void setTempo_confidence(double tempo_confidence) {
		this.tempo_confidence = tempo_confidence;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getTime_signature() {
		return time_signature;
	}

	public void setTime_signature(int time_signature) {
		this.time_signature = time_signature;
	}

	public double getKey_confidence() {
		return key_confidence;
	}

	public void setKey_confidence(double key_confidence) {
		this.key_confidence = key_confidence;
	}

	public double getTime_signature_confidence() {
		return time_signature_confidence;
	}

	public void setTime_signature_confidence(
			double time_signature_confidence) {
		this.time_signature_confidence = time_signature_confidence;
	}

	public String getRhythmstring() {
		return rhythmstring;
	}

	public void setRhythmstring(String rhythmstring) {
		this.rhythmstring = rhythmstring;
	}

	public String getSample_md5() {
		return sample_md5;
	}

	public void setSample_md5(String sample_md5) {
		this.sample_md5 = sample_md5;
	}

	public String getSynchstring() {
		return synchstring;
	}

	public void setSynchstring(String synchstring) {
		this.synchstring = synchstring;
	}

	public String getCodestring() {
		return codestring;
	}

	public void setCodestring(String codestring) {
		this.codestring = codestring;
	}
}