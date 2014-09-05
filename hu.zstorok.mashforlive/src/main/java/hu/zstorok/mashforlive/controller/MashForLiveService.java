package hu.zstorok.mashforlive.controller;

import hu.zstorok.mashforlive.als.ILiveSetBuilder;
import hu.zstorok.mashforlive.als.LiveSet;
import hu.zstorok.mashforlive.als.LiveSetGenerator;
import hu.zstorok.mashforlive.client.EchoNestClient;
import hu.zstorok.mashforlive.client.SoundCloudClient;
import hu.zstorok.mashforlive.client.SoundCloudConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * REST controller implementation. 
 * 
 * @author zstorok
 */
@RestController
public class MashForLiveService {
	
    @Autowired
    private SoundCloudClient soundCloudClient;
    @Autowired
    private EchoNestClient echoNestClient;
    @Autowired
    private LiveSetGenerator liveSetGenerator;
    @Autowired
    private ILiveSetBuilder liveSetBuilder;

	@RequestMapping(value="/service/als", method=RequestMethod.POST, produces="application/octet-stream")
	public @ResponseBody byte[] getAbletonLiveSet(String soundCloudTrackId) {
    	// get track data from SoundCloud
    	JsonNode soundCloudTrack = soundCloudClient.getTrackAsJsonNode(soundCloudTrackId);
    	String soundCloudTrackDownloadLink = soundCloudTrack.get("download_url").asText();
    	
    	// upload track to EchoNest
    	JsonNode trackUploadResponse = echoNestClient.uploadTrack(soundCloudTrackDownloadLink + "?consumer_key=" + SoundCloudConstants.CONSUMER_KEY);
    	String echoNestTrackId = trackUploadResponse.get("response").get("track").get("id").asText();
    	
    	// get EchoNest track audio summary
    	JsonNode echoNestTrackAudioSummary = echoNestClient.getTrackAudioSummaryAsJsonNode(echoNestTrackId);
    	JsonNode echoNestTrackAudioSummaryResponse = echoNestTrackAudioSummary.get("response");
    	String echoNestAnalysisUrl = echoNestTrackAudioSummaryResponse.get("track").get("audio_summary").get("analysis_url").asText();
    	// get EchoNest track analysis data
    	JsonNode echoNestAnalysis = echoNestClient.getAnalysisAsJsonNode(echoNestAnalysisUrl);

    	LiveSet liveSet = liveSetBuilder.build(echoNestAnalysis);
		return liveSetGenerator.generateAls(liveSet);
	}
	
}
