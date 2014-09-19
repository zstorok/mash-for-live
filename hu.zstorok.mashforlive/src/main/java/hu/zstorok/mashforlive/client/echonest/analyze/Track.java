package hu.zstorok.mashforlive.client.echonest.analyze;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO to represent a track in the Echo Nest analysis results.
 * 
 * @author cfstras
 * @author zstorok
 */
public class Track {
	@JsonProperty("num_samples")
	private int numSamples;
	private String decoder;
	@JsonProperty("decoder_version")
	private String decoder_version;
	@JsonProperty("offset_seconds")
	private double offsetSeconds;
	@JsonProperty("window_seconds")
	private double windowSeconds;
	@JsonProperty("analysis_sample_rate")
	private int analysisSampleRate;
	@JsonProperty("analysis_channels")
	private int analysisChannels;
	private int mode;
	@JsonProperty("mode_confidence")
	private double modeConfidence;
	private double duration;
	@JsonProperty("end_of_fade_in")
	private double endOfFadeIn;
	private double loudness;
	@JsonProperty("start_of_fade_out")
	private double startOfFadeOut;
	private double tempo;
	@JsonProperty("tempo_confidence")
	private double tempoConfidence;
	private int key;
	@JsonProperty("time_signature")
	private int timeSignature;
	@JsonProperty("key_confidence")
	private double keyConfidence;
	@JsonProperty("time_signature_confidence")
	private double timeSignatureConfidence;
	private String synchstring;
	@JsonProperty("synch_version")
	private String synchVersion;
	private String echoprintstring;
	@JsonProperty("echoprint_version")
	private String echoprintVersion;
	private String codestring;
	@JsonProperty("code_version")
	private String codeVersion;
	private String rhythmstring;
	@JsonProperty("rhythm_version")
	private String rhythmVersion;
	@JsonProperty("sample_md5")
	private String sampleMd5;

	public int getNumSamples() {
		return numSamples;
	}

	public void setNumSamples(int numSamples) {
		this.numSamples = numSamples;
	}

	public String getDecoder() {
		return decoder;
	}

	public void setDecoder(String decoder) {
		this.decoder = decoder;
	}

	public String getDecoder_version() {
		return decoder_version;
	}

	public void setDecoder_version(String decoder_version) {
		this.decoder_version = decoder_version;
	}

	public double getOffsetSeconds() {
		return offsetSeconds;
	}

	public void setOffsetSeconds(double offsetSeconds) {
		this.offsetSeconds = offsetSeconds;
	}

	public double getWindowSeconds() {
		return windowSeconds;
	}

	public void setWindowSeconds(double windowSeconds) {
		this.windowSeconds = windowSeconds;
	}

	public int getAnalysisSampleRate() {
		return analysisSampleRate;
	}

	public void setAnalysisSampleRate(int analysisSampleRate) {
		this.analysisSampleRate = analysisSampleRate;
	}

	public int getAnalysisChannels() {
		return analysisChannels;
	}

	public void setAnalysisChannels(int analysisChannels) {
		this.analysisChannels = analysisChannels;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public double getEndOfFadeIn() {
		return endOfFadeIn;
	}

	public void setEndOfFadeIn(double endOfFadeIn) {
		this.endOfFadeIn = endOfFadeIn;
	}

	public double getLoudness() {
		return loudness;
	}

	public void setLoudness(double loudness) {
		this.loudness = loudness;
	}

	public double getStartOfFadeOut() {
		return startOfFadeOut;
	}

	public void setStartOfFadeOut(double startOfFadeOut) {
		this.startOfFadeOut = startOfFadeOut;
	}

	public double getTempo() {
		return tempo;
	}

	public void setTempo(double tempo) {
		this.tempo = tempo;
	}

	public double getTempoConfidence() {
		return tempoConfidence;
	}

	public void setTempoConfidence(double tempoConfidence) {
		this.tempoConfidence = tempoConfidence;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getTimeSignature() {
		return timeSignature;
	}

	public void setTimeSignature(int timeSignature) {
		this.timeSignature = timeSignature;
	}

	public double getKeyConfidence() {
		return keyConfidence;
	}

	public void setKeyConfidence(double keyConfidence) {
		this.keyConfidence = keyConfidence;
	}

	public double getTimeSignatureConfidence() {
		return timeSignatureConfidence;
	}

	public void setTimeSignatureConfidence(double timeSignatureConfidence) {
		this.timeSignatureConfidence = timeSignatureConfidence;
	}

	public String getRhythmstring() {
		return rhythmstring;
	}

	public void setRhythmstring(String rhythmstring) {
		this.rhythmstring = rhythmstring;
	}

	public String getSampleMd5() {
		return sampleMd5;
	}

	public void setSampleMd5(String sampleMd5) {
		this.sampleMd5 = sampleMd5;
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

	public String getCodeVersion() {
		return codeVersion;
	}

	public void setCodeVersion(String codeVersion) {
		this.codeVersion = codeVersion;
	}

	public String getSynchVersion() {
		return synchVersion;
	}

	public void setSynchVersion(String synchVersion) {
		this.synchVersion = synchVersion;
	}

	public String getEchoprintstring() {
		return echoprintstring;
	}

	public void setEchoprintstring(String echoprintstring) {
		this.echoprintstring = echoprintstring;
	}

	public String getEchoprintVersion() {
		return echoprintVersion;
	}

	public void setEchoprintVersion(String echoprintVersion) {
		this.echoprintVersion = echoprintVersion;
	}

	public String getRhythmVersion() {
		return rhythmVersion;
	}

	public void setRhythmVersion(String rhythmVersion) {
		this.rhythmVersion = rhythmVersion;
	}
}