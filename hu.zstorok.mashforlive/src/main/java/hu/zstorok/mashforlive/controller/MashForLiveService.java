package hu.zstorok.mashforlive.controller;

import hu.zstorok.mashforlive.als.ILiveSetBuilder;
import hu.zstorok.mashforlive.als.LiveSet;
import hu.zstorok.mashforlive.als.LiveSetGenerator;
import hu.zstorok.mashforlive.client.EchoNestClient;
import hu.zstorok.mashforlive.client.SoundCloudClient;
import hu.zstorok.mashforlive.client.SoundCloudConstants;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

/**
 * REST controller implementation. 
 * 
 * @author zstorok
 */
@RestController
public class MashForLiveService {
	
    private static final String SAMPLES_FOLDER_RELATIVE_PATH = "Samples/";
	@Autowired
    private SoundCloudClient soundCloudClient;
    @Autowired
    private EchoNestClient echoNestClient;
    @Autowired
    private LiveSetGenerator liveSetGenerator;
    @Autowired
    private ILiveSetBuilder liveSetBuilder;

    @RequestMapping(value="/service/zip", method=RequestMethod.POST, produces="application/octet-stream")
    public void getProjectZip(String soundCloudTrackId, HttpServletResponse response) {
    	File projectDirectory = Files.createTempDir();
    	
    	// get track data from SoundCloud
    	JsonNode soundCloudTrack = soundCloudClient.getTrackAsJsonNode(soundCloudTrackId);
    	String soundCloudTrackDownloadUrl = soundCloudTrack.get("download_url").asText() + "?consumer_key=" + SoundCloudConstants.CONSUMER_KEY;
    	
    	try {
    		// download track from SoundCloud
    		File audioFile = downloadAudioFile(projectDirectory, soundCloudTrackDownloadUrl);
    		
        	// upload track to EchoNest
        	JsonNode trackUploadResponse = echoNestClient.uploadTrack(soundCloudTrackDownloadUrl);
        	String echoNestTrackId = trackUploadResponse.get("response").get("track").get("id").asText();
        	
        	// get EchoNest track audio summary
        	JsonNode echoNestTrackAudioSummary = echoNestClient.getTrackAudioSummaryAsJsonNode(echoNestTrackId);
        	JsonNode echoNestTrackAudioSummaryResponse = echoNestTrackAudioSummary.get("response");
        	String echoNestAnalysisUrl = echoNestTrackAudioSummaryResponse.get("track").get("audio_summary").get("analysis_url").asText();
        	// get EchoNest track analysis data
    		JsonNode echoNestAnalysis = echoNestClient.getAnalysisAsJsonNode(
    				echoNestAnalysisUrl, echoNestTrackId);

        	LiveSet liveSet = liveSetBuilder.build(echoNestAnalysis);
    		byte[] alsBytes = liveSetGenerator.generateAls(liveSet);

    		// create ZIP
    		ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
    		zipOutputStream.putNextEntry(new ZipEntry(SAMPLES_FOLDER_RELATIVE_PATH));
    		// add audio file to ZIP
    		ZipEntry audioFileEntry = new ZipEntry(SAMPLES_FOLDER_RELATIVE_PATH + audioFile.getName());
			zipOutputStream.putNextEntry(audioFileEntry);
			FileInputStream audioFileInputStream = new FileInputStream(audioFile);
			ByteStreams.copy(audioFileInputStream, zipOutputStream);
			// add .als to zip
			ZipEntry alsFileEntry = new ZipEntry("mash_for_live.als");
			zipOutputStream.putNextEntry(alsFileEntry);
			zipOutputStream.write(alsBytes);
			zipOutputStream.closeEntry();
			zipOutputStream.close();
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }

	private File downloadAudioFile(File projectDirectory, String soundCloudTrackDownloadUrl) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse httpResponse = httpClient.execute(new HttpGet(soundCloudTrackDownloadUrl));
		// TODO: check response status
		String audioFileName = getAudioFileName(httpResponse);
		byte[] inputBytes = EntityUtils.toByteArray(httpResponse.getEntity());
		File audioFile = new File(projectDirectory, audioFileName);
		audioFile.createNewFile();
		System.out.println(audioFile.getAbsolutePath());
		FileOutputStream audioFileOutputStream = new FileOutputStream(audioFile);
		ByteStreams.copy(new ByteArrayInputStream(inputBytes), audioFileOutputStream);
		audioFileOutputStream.close();
		return audioFile;
	}
    
	private String getAudioFileName(CloseableHttpResponse httpResponse) {
		return httpResponse.getHeaders("Content-Disposition")[0].getElements()[0].getParameterByName("filename").getValue();
	}

	@RequestMapping(value="/service/als", method=RequestMethod.POST, produces="application/octet-stream")
	public @ResponseBody byte[] getAbletonLiveSet(String soundCloudTrackId)
			throws NotDownloadableException {
    	// get track data from SoundCloud
    	JsonNode soundCloudTrack = soundCloudClient.getTrackAsJsonNode(soundCloudTrackId);
    	if (soundCloudTrack.get("download_url") == null) {
			throw new NotDownloadableException();
    	}
    	String soundCloudTrackDownloadLink = soundCloudTrack.get("download_url").asText();
    	
    	// upload track to EchoNest
    	JsonNode trackUploadResponse = echoNestClient.uploadTrack(soundCloudTrackDownloadLink + "?consumer_key=" + SoundCloudConstants.CONSUMER_KEY);
    	String echoNestTrackId = trackUploadResponse.get("response").get("track").get("id").asText();
    	
    	// get EchoNest track audio summary
    	JsonNode echoNestTrackAudioSummary = echoNestClient.getTrackAudioSummaryAsJsonNode(echoNestTrackId);
    	JsonNode echoNestTrackAudioSummaryResponse = echoNestTrackAudioSummary.get("response");
    	String echoNestAnalysisUrl = echoNestTrackAudioSummaryResponse.get("track").get("audio_summary").get("analysis_url").asText();
    	// get EchoNest track analysis data
		JsonNode echoNestAnalysis = echoNestClient.getAnalysisAsJsonNode(
				echoNestAnalysisUrl, echoNestTrackId);

    	LiveSet liveSet = liveSetBuilder.build(echoNestAnalysis);
		return liveSetGenerator.generateAls(liveSet);
	}

	@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Track is not downloadable")
	@SuppressWarnings("serial")
	static class NotDownloadableException extends Exception {
		public NotDownloadableException() {
			super();
		}

		public NotDownloadableException(String arg0) {
			super(arg0);
		}
	}
}
