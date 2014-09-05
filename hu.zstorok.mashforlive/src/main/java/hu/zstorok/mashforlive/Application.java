package hu.zstorok.mashforlive;

import hu.zstorok.mashforlive.client.EchoNestClient;
import hu.zstorok.mashforlive.client.SoundCloudClient;
import hu.zstorok.mashforlive.client.SoundCloudConstants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.JsonNode;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {
    
    @Autowired
    private SoundCloudClient soundCloudClient;
    @Autowired
    private EchoNestClient echoNestClient;

//    @PostConstruct
    public void run() {
    	// get track data from SoundCloud
    	JsonNode soundCloudTrack = soundCloudClient.getTrackAsJsonNode("138111002");
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
    	System.out.println(echoNestAnalysis.get("track").get("synchstring").asText());
    	File analysisFile = new File("analysis.js");
        try {
			FileOutputStream fos = new FileOutputStream(analysisFile);
			OutputStreamWriter osw = new OutputStreamWriter(fos);    
			Writer w = new BufferedWriter(osw);
			w.write(echoNestAnalysis.toString());
			w.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	// process EchoNest analysis data
//    	processBeats(analysis);
    	processBars(echoNestAnalysis);
    	processSections(echoNestAnalysis);
    }

	private void processSections(JsonNode analysis) {
		System.out.println("******** sections");
		JsonNode sections = analysis.get("sections");
    	Iterator<JsonNode> elements = sections.elements();
    	while (elements.hasNext()) {
			JsonNode section = (JsonNode) elements.next();
			String start = section.get("start").asText();
			String duration = section.get("duration").asText();
			double confidence = section.get("confidence").asDouble();
			System.out.println(start + ", " + duration + ", " + confidence);
		}
	}

	private void processBeats(JsonNode analysis) {
		System.out.println("******** beats");
		JsonNode beats = analysis.get("beats");
    	Iterator<JsonNode> elements = beats.elements();
    	int beatCounter = 0;
    	String row = "<WarpMarker SecTime=\"" + "0.00000" + "\" BeatTime=\"" + beatCounter++ + "\" />";
    	System.out.println(row);
    	while (elements.hasNext()) {
			JsonNode beat = (JsonNode) elements.next();
			String time = beat.get("start").asText();
			row = "<WarpMarker SecTime=\"" + time + "\" BeatTime=\"" + beatCounter++ + "\" />";
			System.out.println(row);
		}
	}

	private void processBars(JsonNode analysis) {
		System.out.println("******** bars");
		JsonNode bars = analysis.get("bars");
    	Iterator<JsonNode> elements = bars.elements();
    	int barCounter = 0;
    	String row = "<WarpMarker SecTime=\"" + "0.00000" + "\" BeatTime=\"" + barCounter++ + "\" />";
    	System.out.println(row);
    	while (elements.hasNext()) {
    		JsonNode beat = (JsonNode) elements.next();
    		String time = beat.get("start").asText();
    		row = "<WarpMarker SecTime=\"" + time + "\" BeatTime=\"" + (4 * barCounter++) + "\" />";
    		System.out.println(row);
    	}
	}
    
	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
