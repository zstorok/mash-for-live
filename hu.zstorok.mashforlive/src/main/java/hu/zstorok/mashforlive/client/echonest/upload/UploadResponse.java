package hu.zstorok.mashforlive.client.echonest.upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadResponse {

	public static class Status {
		private String version;
		private String code;
		private String message;

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}

	public static class Track {
		private String id;
		private String status;
		private String artist;
		private String title;
		private String release;
		@JsonProperty("audio_md5")
		private String audioMd5;
		private int bitrate;
		private int samplerate;
		private String md5;

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getArtist() {
			return artist;
		}

		public void setArtist(String artist) {
			this.artist = artist;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getRelease() {
			return release;
		}

		public void setRelease(String release) {
			this.release = release;
		}

		public String getAudioMd5() {
			return audioMd5;
		}

		public void setAudioMd5(String audioMd5) {
			this.audioMd5 = audioMd5;
		}

		public int getBitrate() {
			return bitrate;
		}

		public void setBitrate(int bitrate) {
			this.bitrate = bitrate;
		}

		public int getSamplerate() {
			return samplerate;
		}

		public void setSamplerate(int samplerate) {
			this.samplerate = samplerate;
		}

		public String getMd5() {
			return md5;
		}

		public void setMd5(String md5) {
			this.md5 = md5;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

	}

	private Track track;
	private Status status;

	public Track getTrack() {
		return track;
	}

	public void setTrack(Track track) {
		this.track = track;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
