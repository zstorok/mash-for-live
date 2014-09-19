package hu.zstorok.mashforlive.client.echonest.analyze;

/**
 * POJO to represent a musical element in the Echo Nest analysis results.
 * 
 * @author cfstras
 * @author zstorok
 */
public abstract class MusicElement {
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