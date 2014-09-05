package hu.zstorok.mashforlive.client;

import static hu.zstorok.mashforlive.client.SoundCloudConstants.TRACK_BY_ID_URL_TEMPLATE;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Service to encapsulate the interaction with the SoundCloud API.
 * 
 * @author zstorok
 */
@Service
public class SoundCloudClient {

	private final RestTemplate restTemplate = new RestTemplate();
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getTrack(String trackId) {
		Map<String, Object> map = (Map<String, Object>)restTemplate.getForObject(TRACK_BY_ID_URL_TEMPLATE, 
				Map.class, trackId, SoundCloudConstants.CONSUMER_KEY);
		return map;
	}
	
	public JsonNode getTrackAsJsonNode(String trackId) {
		JsonNode jsonNode = restTemplate.getForObject(TRACK_BY_ID_URL_TEMPLATE, 
				JsonNode.class, trackId, SoundCloudConstants.CONSUMER_KEY);
		return jsonNode;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getTracks(Map<String, String> options) {
		Map<String, Object>[] maps = (Map<String, Object>[])restTemplate.getForObject("https://api.soundcloud.com/tracks.json?consumer_key={consumer_key}?filter=downloadable", 
				Map[].class, SoundCloudConstants.CONSUMER_KEY);
		return Arrays.asList(maps);
	}
}
