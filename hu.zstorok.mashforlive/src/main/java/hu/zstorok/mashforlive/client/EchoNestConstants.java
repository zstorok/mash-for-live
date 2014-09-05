package hu.zstorok.mashforlive.client;

/**
 * Constants related to the EchoNest API.
 * 
 * @author zstorok
 */
public final class EchoNestConstants {

    public static final String API_KEY = "ZNN5X4DC5SRSKATSO";
    public static final String CONSUMER_KEY = "865631306b9f07752d406cc51cb8ace1";
    public static final String SHARED_SECRET = "irFXvFWsR9Ksj5l2QtQdKA";
    
    public static final String BASE_URL = "http://developer.echonest.com/api/v4";
	public static final String TRACK_AUDIO_SUMMARY_URL_TEMPLATE = "/track/profile?api_key={api_key}&format=json&bucket=audio_summary&id={id}";
	public static final String TRACK_UPLOAD_URL_TEMPLATE = "/track/upload?api_key={api_key}&url={url}";
}
