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
import org.springframework.web.bind.annotation.ExceptionHandler;
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

	private static final String ALS_FILE_RELATIVE_PATH = "mash_for_live.als";
	private static final String SAMPLES_FOLDER_RELATIVE_PATH = "Samples/";

	@Autowired
	private SoundCloudClient soundCloudClient;
	@Autowired
	private EchoNestClient echoNestClient;
	@Autowired
	private LiveSetGenerator liveSetGenerator;
	@Autowired
	private ILiveSetBuilder liveSetBuilder;

	@RequestMapping(value = "/service/zip", method = RequestMethod.POST, produces = "application/octet-stream")
	public void getProjectZip(String soundCloudTrackId, HttpServletResponse response) throws NotDownloadableException,
			DownloadFailedException {
		File projectDirectory = Files.createTempDir();

		// get track data from SoundCloud
		JsonNode soundCloudTrack = soundCloudClient.getTrackAsJsonNode(soundCloudTrackId);
		String soundCloudTrackDownloadUrl = soundCloudTrack.get("download_url").asText() + "?consumer_key="
				+ SoundCloudConstants.CONSUMER_KEY;

		// download track from SoundCloud
		File audioFile = downloadAudioFile(projectDirectory, soundCloudTrackDownloadUrl);

		// upload track to EchoNest
		JsonNode trackUploadResponse = echoNestClient.uploadTrack(soundCloudTrackDownloadUrl);
		String echoNestTrackId = trackUploadResponse.get("response").get("track").get("id").asText();

		// get EchoNest track audio summary
		JsonNode echoNestTrackAudioSummary = echoNestClient.getTrackAudioSummaryAsJsonNode(echoNestTrackId);
		JsonNode echoNestTrackAudioSummaryResponse = echoNestTrackAudioSummary.get("response");
		String echoNestAnalysisUrl = echoNestTrackAudioSummaryResponse.get("track").get("audio_summary")
				.get("analysis_url").asText();
		// get EchoNest track analysis data
		JsonNode echoNestAnalysis = echoNestClient.getAnalysisAsJsonNode(echoNestAnalysisUrl, echoNestTrackId);

		LiveSet liveSet = liveSetBuilder.build(echoNestAnalysis);
		byte[] alsBytes = liveSetGenerator.generateAls(liveSet);

		// create ZIP
		ZipOutputStream zipOutputStream = null;
		try {
			zipOutputStream = new ZipOutputStream(response.getOutputStream());
			zipOutputStream.putNextEntry(new ZipEntry(SAMPLES_FOLDER_RELATIVE_PATH));
			// add audio file to ZIP
			ZipEntry audioFileEntry = new ZipEntry(SAMPLES_FOLDER_RELATIVE_PATH + audioFile.getName());
			zipOutputStream.putNextEntry(audioFileEntry);
			FileInputStream audioFileInputStream = new FileInputStream(audioFile);
			ByteStreams.copy(audioFileInputStream, zipOutputStream);
			// add .als to zip
			ZipEntry alsFileEntry = new ZipEntry(ALS_FILE_RELATIVE_PATH);
			zipOutputStream.putNextEntry(alsFileEntry);
			zipOutputStream.write(alsBytes);
		} catch (IOException e) {
			throw new DownloadFailedException("Error when downloading project ZIP file.");
		} finally {
			if (zipOutputStream != null) {
				try {
					zipOutputStream.closeEntry();
					zipOutputStream.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	private File downloadAudioFile(File projectDirectory, String soundCloudTrackDownloadUrl)
			throws DownloadFailedException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			CloseableHttpResponse httpResponse = httpClient.execute(new HttpGet(soundCloudTrackDownloadUrl));
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				throw new DownloadFailedException("Error when downloading track from SoundCloud; status code: "
						+ statusCode);
			}
			String audioFileName = getAudioFileName(httpResponse);
			byte[] inputBytes = EntityUtils.toByteArray(httpResponse.getEntity());
			FileOutputStream audioFileOutputStream = null;
			File audioFile = null;
			try {
				audioFile = new File(projectDirectory, audioFileName);
				audioFile.createNewFile();
				audioFileOutputStream = new FileOutputStream(audioFile);
				ByteStreams.copy(new ByteArrayInputStream(inputBytes), audioFileOutputStream);
			} catch (IOException e) {
				throw new DownloadFailedException("Error when writing audio file downloaded from SoundCloud.");
			} finally {
				if (audioFileOutputStream != null) {
					audioFileOutputStream.close();
				}
			}
			return audioFile;
		} catch (IOException e) {
			throw new DownloadFailedException("Error when downloading track from SoundCloud.");
		}
	}

	private String getAudioFileName(CloseableHttpResponse httpResponse) {
		return httpResponse.getHeaders("Content-Disposition")[0].getElements()[0].getParameterByName("filename")
				.getValue();
	}

	@RequestMapping(value = "/service/als", method = RequestMethod.POST, produces = "application/octet-stream")
	public @ResponseBody byte[] getAbletonLiveSet(String soundCloudTrackId) throws NotDownloadableException {
		// get track data from SoundCloud
		JsonNode soundCloudTrack = soundCloudClient.getTrackAsJsonNode(soundCloudTrackId);
		if (soundCloudTrack.get("download_url") == null) {
			throw new NotDownloadableException();
		}
		String soundCloudTrackDownloadLink = soundCloudTrack.get("download_url").asText();

		// upload track to EchoNest
		JsonNode trackUploadResponse = echoNestClient.uploadTrack(soundCloudTrackDownloadLink + "?consumer_key="
				+ SoundCloudConstants.CONSUMER_KEY);
		String echoNestTrackId = trackUploadResponse.get("response").get("track").get("id").asText();

		// get EchoNest track audio summary
		JsonNode echoNestTrackAudioSummary = echoNestClient.getTrackAudioSummaryAsJsonNode(echoNestTrackId);
		JsonNode echoNestTrackAudioSummaryResponse = echoNestTrackAudioSummary.get("response");
		String echoNestAnalysisUrl = echoNestTrackAudioSummaryResponse.get("track").get("audio_summary")
				.get("analysis_url").asText();
		// get EchoNest track analysis data
		JsonNode echoNestAnalysis = echoNestClient.getAnalysisAsJsonNode(echoNestAnalysisUrl, echoNestTrackId);

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

	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Track download failed")
	@SuppressWarnings("serial")
	static class DownloadFailedException extends Exception {
		public DownloadFailedException() {
			super();
		}

		public DownloadFailedException(String arg0) {
			super(arg0);
		}
	}
	
	@ExceptionHandler(value=Exception.class)
	public void handleGenericException() {}
}
