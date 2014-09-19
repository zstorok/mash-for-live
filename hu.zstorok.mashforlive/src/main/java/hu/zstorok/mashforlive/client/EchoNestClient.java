package hu.zstorok.mashforlive.client;

import static hu.zstorok.mashforlive.client.EchoNestConstants.API_KEY;
import static hu.zstorok.mashforlive.client.EchoNestConstants.BASE_URL;
import static hu.zstorok.mashforlive.client.EchoNestConstants.TRACK_AUDIO_STATUS_URL_TEMPLATE;
import static hu.zstorok.mashforlive.client.EchoNestConstants.TRACK_AUDIO_SUMMARY_URL_TEMPLATE;
import static hu.zstorok.mashforlive.client.EchoNestConstants.TRACK_UPLOAD_URL_TEMPLATE;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Service to encapsulate the interaction with the EchoNest API.
 * 
 * @author zstorok
 */
@Service
public class EchoNestClient {

	@Autowired
	private ObjectMapper mapper;
	private final RestTemplate restTemplate = new RestTemplate();

	public JsonNode uploadTrack(String url) {
		return restTemplate.postForObject(BASE_URL + TRACK_UPLOAD_URL_TEMPLATE, null, JsonNode.class, API_KEY, url);
	}
	
	public JsonNode getTrackAudioSummaryAsJsonNode(String id) {
		return restTemplate
				.getForObject(BASE_URL + TRACK_AUDIO_SUMMARY_URL_TEMPLATE,
						JsonNode.class, API_KEY, id);
	}

	public EchoNestTrackStatus getTrackStatus(String id) {
		JsonNode res = restTemplate.getForObject(BASE_URL
				+ TRACK_AUDIO_STATUS_URL_TEMPLATE,
				JsonNode.class, API_KEY, id);
		String statusAsText = res.get("response").get("track").get("status").asText();
		EchoNestTrackStatus status = EchoNestTrackStatus.fromJsonValue(statusAsText);
		return status;
	}

	public EchoNestAnalysis getAnalysis(String analysisUrl, String trackId) {
		System.out.println(analysisUrl);
		try {
			URI uri = new URI(analysisUrl);
			EchoNestAnalysis response = new RestTemplate().getForObject(uri,
					EchoNestAnalysis.class);
			return response;
		} catch (HttpStatusCodeException e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
				EchoNestTrackStatus status;
				do {
					status = getTrackStatus(trackId);
					System.out.println("track status: " + status);
					if (status == EchoNestTrackStatus.PENDING) {
						try {
							Thread.sleep(1500);
						} catch (InterruptedException e1) {
						}
					}
				} while (status == EchoNestTrackStatus.PENDING);
				switch (status) {
				case UNKNOWN:
					throw new WebServiceClientException(
							"Submit error, track has never been received", e);
				case COMPLETE:
					return getAnalysis(analysisUrl, trackId);
				case ERROR:
				default:
					throw new WebServiceClientException(e);
				}
			} else {
				throw new WebServiceClientException(e);
			}
		} catch (URISyntaxException e) {
			throw new WebServiceClientException(e);
		}
	}
}
