package hu.zstorok.mashforlive.client;

public enum EchoNestTrackStatus {
	/**
	 * The track has never been submitted for analysis. Upload the track for
	 * analysis.
	 */
	unknown,

	/**
	 * The track has been uploaded for analysis, but the analysis is not yet
	 * ready. Wait for status to change.
	 */
	pending,

	/**
	 * The track has been analyzed.
	 */
	complete,

	/**
	 * The track could not be analyzed.
	 */
	error;

	public static EchoNestTrackStatus from(String raw) {
		EchoNestTrackStatus s = valueOf(raw);
		if (s != null) {
			return s;
		}
		for (EchoNestTrackStatus p : values()) {
			if (p.name().toLowerCase().equals(raw.toLowerCase())) {
				return p;
			}
		}
		return null;
	}
}
