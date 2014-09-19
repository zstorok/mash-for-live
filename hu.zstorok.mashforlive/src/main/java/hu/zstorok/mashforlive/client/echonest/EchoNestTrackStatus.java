package hu.zstorok.mashforlive.client.echonest;

import com.google.common.base.Preconditions;

/**
 * Enum to represent the status of a track in the Echo Nest service.
 * 
 * @author cfstras
 * @author zstorok - introduced JSON value field
 */
public enum EchoNestTrackStatus {
	/**
	 * The track has never been submitted for analysis. Upload the track for
	 * analysis.
	 */
	UNKNOWN("unknown"),

	/**
	 * The track has been uploaded for analysis, but the analysis is not yet
	 * ready. Wait for status to change.
	 */
	PENDING("pending"),

	/**
	 * The track has been analyzed.
	 */
	COMPLETE("complete"),

	/**
	 * The track could not be analyzed.
	 */
	ERROR("error");

	private final String jsonValue;

	private EchoNestTrackStatus(String jsonValue) {
		this.jsonValue = jsonValue;
	}
	
	public static EchoNestTrackStatus fromJsonValue(String jsonValue) {
		Preconditions.checkNotNull(jsonValue, "JSON value must not be null.");
		for (EchoNestTrackStatus value : values()) {
			if (value.jsonValue.equals(jsonValue)) {
				return value;
			}
		}
		throw new IllegalArgumentException("Unexpected JSON value: " + jsonValue);
	}
}
