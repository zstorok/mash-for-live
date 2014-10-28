package hu.zstorok.mashforlive.client.echonest;

import static hu.zstorok.mashforlive.client.echonest.EchoNestConstants.API_KEY;
import static hu.zstorok.mashforlive.client.echonest.EchoNestConstants.BASE_URL;
import static hu.zstorok.mashforlive.client.echonest.EchoNestConstants.TRACK_AUDIO_STATUS_URL_TEMPLATE;
import static hu.zstorok.mashforlive.client.echonest.EchoNestConstants.TRACK_AUDIO_SUMMARY_URL_TEMPLATE;
import static hu.zstorok.mashforlive.client.echonest.EchoNestConstants.TRACK_UPLOAD_URL_TEMPLATE;
import hu.zstorok.mashforlive.client.WebServiceClientException;
import hu.zstorok.mashforlive.client.echonest.analyze.Analysis;
import hu.zstorok.mashforlive.client.echonest.analyze.EchoNestTrackStatus;
import hu.zstorok.mashforlive.client.echonest.upload.UploadResponseWrapper;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(EchoNestClient.class);
	
	@Autowired
	private ObjectMapper mapper;
	private final RestTemplate restTemplate = new RestTemplate();

	public UploadResponseWrapper uploadTrack(String url) {
		LOGGER.info("Uploading track at URL: " + url);
		return restTemplate.postForObject(BASE_URL + TRACK_UPLOAD_URL_TEMPLATE, null, UploadResponseWrapper.class, API_KEY, url);
	}
	
	public JsonNode getTrackAudioSummaryAsJsonNode(String id) {
		LOGGER.info("Retrieving track audio summary for ID: " + id);
		return restTemplate
				.getForObject(BASE_URL + TRACK_AUDIO_SUMMARY_URL_TEMPLATE,
						JsonNode.class, API_KEY, id);
	}

	public EchoNestTrackStatus getTrackStatus(String id) {
		LOGGER.info("Retrieving track status for ID: " + id);
		JsonNode res = restTemplate.getForObject(BASE_URL
				+ TRACK_AUDIO_STATUS_URL_TEMPLATE,
				JsonNode.class, API_KEY, id);
		String statusAsText = res.get("response").get("track").get("status").asText();
		EchoNestTrackStatus status = EchoNestTrackStatus.fromJsonValue(statusAsText);
		return status;
	}

	public Analysis getAnalysis(String analysisUrl, String trackId) {
		LOGGER.info("Retrieving analysis results for ID: " + trackId);
		try {
			URI uri = new URI(analysisUrl);
			Analysis response = new RestTemplate().getForObject(uri,
					Analysis.class);
			return response;
		} catch (HttpStatusCodeException e) {
			if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
				EchoNestTrackStatus status;
				do {
					status = getTrackStatus(trackId);
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
