package hu.zstorok.mashforlive.client;

import static hu.zstorok.mashforlive.client.EchoNestConstants.API_KEY;
import static hu.zstorok.mashforlive.client.EchoNestConstants.BASE_URL;
import static hu.zstorok.mashforlive.client.EchoNestConstants.TRACK_AUDIO_SUMMARY_URL_TEMPLATE;
import static hu.zstorok.mashforlive.client.EchoNestConstants.TRACK_UPLOAD_URL_TEMPLATE;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Service to encapsulate the interaction with the EchoNest API.
 * 
 * @author zstorok
 */
@Service
public class EchoNestClient {

	private final RestTemplate restTemplate = new RestTemplate();

	public JsonNode uploadTrack(String url) {
		return restTemplate.postForObject(BASE_URL + TRACK_UPLOAD_URL_TEMPLATE, null, JsonNode.class, API_KEY, url);
	}
	
	public JsonNode getTrackAudioSummaryAsJsonNode(String id) {
		return restTemplate.getForObject(BASE_URL + TRACK_AUDIO_SUMMARY_URL_TEMPLATE,	JsonNode.class, API_KEY, id);
	}

	public JsonNode getAnalysisAsJsonNode(String analysisUrl) {
		System.out.println(analysisUrl);
		try {
			URI uri = new URI(analysisUrl);
			return new RestTemplate().getForObject(uri, JsonNode.class);
		} catch (RestClientException e) {
			throw new WebServiceClientException(e);
		} catch (URISyntaxException e) {
			throw new WebServiceClientException(e);
		}
	}
}
