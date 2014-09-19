package hu.zstorok.mashforlive.client.echonest.analyze;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO to represent the metadata in the Echo Nest analysis results.
 * 
 * @author cfstras
 * @author zstorok
 */
public class Meta {
	@JsonProperty("analyzer_version")
	private String analyzerVersion;
	private String platform;
	@JsonProperty("detailed_status")
	private String detailedStatus;
	private String album, artist, filename, genre, title;
	private int bitrate;
	@JsonProperty("sample_rate")
	private int sampleRate;
	private double seconds;
	@JsonProperty("status_code")
	private int statusCode;
	private long timestamp;
	@JsonProperty("analysis_time")
	private double analysisTime;

	public String getAnalyzerVersion() {
		return analyzerVersion;
	}

	public void setAnalyzerVersion(String analyzerVersion) {
		this.analyzerVersion = analyzerVersion;
	}

	public String getDetailedStatus() {
		return detailedStatus;
	}

	public void setDetailedStatus(String detailedStatus) {
		this.detailedStatus = detailedStatus;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getBitrate() {
		return bitrate;
	}

	public void setBitrate(int bitrate) {
		this.bitrate = bitrate;
	}

	public int getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
	}

	public double getSeconds() {
		return seconds;
	}

	public void setSeconds(double seconds) {
		this.seconds = seconds;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public double getAnalysisTime() {
		return analysisTime;
	}

	public void setAnalysisTime(double analysisTime) {
		this.analysisTime = analysisTime;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

}