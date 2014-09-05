package hu.zstorok.mashforlive.client;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EchoNestAnalysis {
	private List<Bar> bars;
	private List<Beat> beats;
	private List<Section> sections;
	private List<Segment> segments;
	private List<Tatum> tatums;
	private Track track;
	private Meta meta;

	public List<Bar> getBars() {
		return bars;
	}

	public void setBars(List<Bar> bars) {
		this.bars = bars;
	}

	public List<Beat> getBeats() {
		return beats;
	}

	public void setBeats(List<Beat> beats) {
		this.beats = beats;
	}

	public List<Section> getSections() {
		return sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}

	public List<Segment> getSegments() {
		return segments;
	}

	public void setSegments(List<Segment> segments) {
		this.segments = segments;
	}

	public List<Tatum> getTatums() {
		return tatums;
	}

	public void setTatums(List<Tatum> tatums) {
		this.tatums = tatums;
	}

	public Track getTrack() {
		return track;
	}

	public void setTrack(Track track) {
		this.track = track;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static abstract class MusicElement {
		private double confidence, duration, start;

		public double getConfidence() {
			return confidence;
		}

		public void setConfidence(double confidence) {
			this.confidence = confidence;
		}

		public double getDuration() {
			return duration;
		}

		public void setDuration(double duration) {
			this.duration = duration;
		}

		public double getStart() {
			return start;
		}

		public void setStart(double start) {
			this.start = start;
		}

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Bar extends MusicElement {
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Beat extends MusicElement {
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Section extends MusicElement {
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

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Segment extends MusicElement {
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

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Tatum extends MusicElement {

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Track {
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

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Meta {
		private String album, artist, filename, genre, title;
		private double seconds;

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

		public double getSeconds() {
			return seconds;
		}

		public void setSeconds(double seconds) {
			this.seconds = seconds;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Summary {
		private double acousticness, danceability, energy, liveness,
				speechiness, valence;
		private double duration, loudness, tempo;
		private int key, mode, time_signature;
		private String analysis_url;

		public double getAcousticness() {
			return acousticness;
		}

		public void setAcousticness(double acousticness) {
			this.acousticness = acousticness;
		}

		public double getDanceability() {
			return danceability;
		}

		public void setDanceability(double danceability) {
			this.danceability = danceability;
		}

		public double getEnergy() {
			return energy;
		}

		public void setEnergy(double energy) {
			this.energy = energy;
		}

		public double getLiveness() {
			return liveness;
		}

		public void setLiveness(double liveness) {
			this.liveness = liveness;
		}

		public double getSpeechiness() {
			return speechiness;
		}

		public void setSpeechiness(double speechiness) {
			this.speechiness = speechiness;
		}

		public double getValence() {
			return valence;
		}

		public void setValence(double valence) {
			this.valence = valence;
		}

		public double getDuration() {
			return duration;
		}

		public void setDuration(double duration) {
			this.duration = duration;
		}

		public double getLoudness() {
			return loudness;
		}

		public void setLoudness(double loudness) {
			this.loudness = loudness;
		}

		public double getTempo() {
			return tempo;
		}

		public void setTempo(double tempo) {
			this.tempo = tempo;
		}

		public int getKey() {
			return key;
		}

		public void setKey(int key) {
			this.key = key;
		}

		public int getMode() {
			return mode;
		}

		public void setMode(int mode) {
			this.mode = mode;
		}

		public int getTime_signature() {
			return time_signature;
		}

		public void setTime_signature(int time_signature) {
			this.time_signature = time_signature;
		}

		public String getAnalysis_url() {
			return analysis_url;
		}

		public void setAnalysis_url(String analysis_url) {
			this.analysis_url = analysis_url;
		}

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Info {
		private String artist, id, tag, title, url;

		public String getArtist() {
			return artist;
		}

		public void setArtist(String artist) {
			this.artist = artist;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTag() {
			return tag;
		}

		public void setTag(String tag) {
			this.tag = tag;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}
}

