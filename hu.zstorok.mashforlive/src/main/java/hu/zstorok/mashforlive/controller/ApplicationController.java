package hu.zstorok.mashforlive.controller;

import hu.zstorok.mashforlive.als.ILiveSetBuilder;
import hu.zstorok.mashforlive.als.LiveSet;
import hu.zstorok.mashforlive.als.LiveSetGenerator;
import hu.zstorok.mashforlive.client.echonest.EchoNestClient;
import hu.zstorok.mashforlive.client.echonest.analyze.Analysis;
import hu.zstorok.mashforlive.client.echonest.upload.UploadResponseWrapper;
import hu.zstorok.mashforlive.client.soundcloud.SoundCloudClient;
import hu.zstorok.mashforlive.client.soundcloud.SoundCloudConstants;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.MapMaker;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

/**
 * REST controller implementation.
 * 
 * @author zstorok
 */
@RestController
public class ApplicationController {

	private static Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);
	
	private static final String ZIP_FILE_EXTENSION = ".zip";
	private static final String TEMP_ZIP_FILE_NAME_PREFIX = "mash_for_live_";
	private static final String DOWNLOADED_ZIP_FILE_NAME_SUFFIX = " (Mash for Live) Project";
	private static final String ALS_FILE_NAME_SUFFIX = " (Mash for Live)";
	private static final String SAMPLES_FOLDER_RELATIVE_PATH = "Samples/";
	private static final String UNKNOWN_TRACK_TITLE = "Unknown Track";
	private static final String ALS_FILE_EXTENSION = ".als";
	
	private final Map<UUID, String> uuidToTrackNameMap = new MapMaker().makeMap();

	@Autowired
	private SoundCloudClient soundCloudClient;
	@Autowired
	private EchoNestClient echoNestClient;
	@Autowired
	private LiveSetGenerator liveSetGenerator;
	@Autowired
	private ILiveSetBuilder liveSetBuilder;
	
	private File tempDirectory;
	
	@PostConstruct
	public void init() {
		tempDirectory = Files.createTempDir();
		LOGGER.info("Using temp directory: " + tempDirectory);
	}
	
	@RequestMapping(value = "/service/zip/{id}", method = RequestMethod.GET, produces = "application/octet-stream")
	public void getProjectZip(@PathVariable("id") String id, HttpServletResponse response) throws NotFoundException, DownloadFailedException {
		LOGGER.info("Serving project ZIP for ID: " + id);
		String tempZipFileName = TEMP_ZIP_FILE_NAME_PREFIX + id + ZIP_FILE_EXTENSION;
		File tempZipFile = new File(tempDirectory, tempZipFileName);
		if (!tempZipFile.exists()) {
			throw new NotFoundException();
		}
		
		UUID uuid = UUID.fromString(id);
		String trackName;
		if (uuidToTrackNameMap.containsKey(uuid)) {
			trackName = uuidToTrackNameMap.get(uuid);
		} else {
			trackName = tempZipFileName;
		}
		response.addHeader("Content-Disposition", "attachment; filename=" + trackName + DOWNLOADED_ZIP_FILE_NAME_SUFFIX
				+ ZIP_FILE_EXTENSION);
		response.setContentType("application/octet-stream");
		
		ServletOutputStream responseOutputStream = null;
		FileInputStream fileInputStream = null;
		try {
			responseOutputStream = response.getOutputStream();
			fileInputStream = new FileInputStream(tempZipFile);
			ByteStreams.copy(fileInputStream, responseOutputStream);
		} catch (IOException e) {
			throw new DownloadFailedException("Error when downloading project ZIP file.");
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					// ignore
				}
			}
			if (responseOutputStream != null) {
				try {
					responseOutputStream.close();
				} catch (IOException e) {
					// ignore
				}
			}
			// clean up both after successful and failed download
			uuidToTrackNameMap.remove(uuid);
			tempZipFile.delete();
			LOGGER.info("Deleted temporary ZIP file: " + tempZipFile);
		}
	}
	
	@RequestMapping(value = "/service/zip", method = RequestMethod.POST, produces = "application/octet-stream")
	public String generateProjectZip(String soundCloudTrackId) throws NotDownloadableException,
			DownloadFailedException {
		LOGGER.info("Generating project ZIP for SoundCloud Track ID: " + soundCloudTrackId);

		// get track data from SoundCloud
		JsonNode soundCloudTrack = soundCloudClient.getTrackAsJsonNode(soundCloudTrackId);
		String soundCloudTrackDownloadUrl = soundCloudTrack.get("download_url").asText() + "?consumer_key="
				+ SoundCloudConstants.CONSUMER_KEY;
		String trackTitle;
		if (soundCloudTrack.get("title") != null && soundCloudTrack.get("title").isTextual()) {
			trackTitle = soundCloudTrack.get("title").asText().trim().replaceAll("[^a-zA-Z0-9-_\\.]", "_");
		} else {
			trackTitle = UNKNOWN_TRACK_TITLE;
		}

		// download track from SoundCloud
		File audioFile = downloadAudioFile(tempDirectory, soundCloudTrackDownloadUrl);

		// upload track to EchoNest
		UploadResponseWrapper trackUploadResponse = echoNestClient.uploadTrack(soundCloudTrackDownloadUrl);
		String echoNestTrackId = trackUploadResponse.getResponse().getTrack().getId();

		// get EchoNest track audio summary
		JsonNode echoNestTrackAudioSummary = echoNestClient.getTrackAudioSummaryAsJsonNode(echoNestTrackId);
		JsonNode echoNestTrackAudioSummaryResponse = echoNestTrackAudioSummary.get("response");
		String echoNestAnalysisUrl = echoNestTrackAudioSummaryResponse.get("track").get("audio_summary")
				.get("analysis_url").asText();
		// get EchoNest track analysis data
		Analysis echoNestAnalysis = echoNestClient.getAnalysis(
				echoNestAnalysisUrl, echoNestTrackId);

		LiveSet liveSet = liveSetBuilder.build(echoNestAnalysis, audioFile.getName());
		byte[] alsBytes = liveSetGenerator.generateAls(liveSet);

		// create ZIP
		ZipOutputStream zipOutputStream = null;
		try {
			UUID tempZipUuid = UUID.randomUUID();
			String tempZipId = tempZipUuid.toString();
			File tempZipFile = new File(tempDirectory, TEMP_ZIP_FILE_NAME_PREFIX + tempZipId + ZIP_FILE_EXTENSION);
			FileOutputStream tempZipFileOutputStream = new FileOutputStream(tempZipFile);
			zipOutputStream = new ZipOutputStream(tempZipFileOutputStream);
			zipOutputStream.putNextEntry(new ZipEntry(SAMPLES_FOLDER_RELATIVE_PATH));
			// add audio file to ZIP
			ZipEntry audioFileEntry = new ZipEntry(SAMPLES_FOLDER_RELATIVE_PATH + audioFile.getName());
			zipOutputStream.putNextEntry(audioFileEntry);
			FileInputStream audioFileInputStream = new FileInputStream(audioFile);
			ByteStreams.copy(audioFileInputStream, zipOutputStream);
			// add .als to zip
			ZipEntry alsFileEntry = new ZipEntry(trackTitle + ALS_FILE_NAME_SUFFIX + ALS_FILE_EXTENSION);
			zipOutputStream.putNextEntry(alsFileEntry);
			zipOutputStream.write(alsBytes);
			uuidToTrackNameMap.put(tempZipUuid, trackTitle);
			return "/service/zip/" + tempZipId;
		} catch (IOException e) {
			throw new DownloadFailedException("Error when generating project ZIP file.");
		} finally {
			if (zipOutputStream != null) {
				try {
					zipOutputStream.closeEntry();
					zipOutputStream.close();
				} catch (IOException e) {
					// ignore
				}
			}
			// clean up downloaded audio file
			audioFile.delete();
			LOGGER.info("Deleted temporary audio file: " + audioFile);
		}
	}

	private File downloadAudioFile(File projectDirectory, String soundCloudTrackDownloadUrl)
			throws DownloadFailedException {
		LOGGER.info("Downloading track from SoundCloud: " + soundCloudTrackDownloadUrl);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			CloseableHttpResponse httpResponse = httpClient.execute(new HttpGet(soundCloudTrackDownloadUrl));
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				String message = "Error when downloading track from SoundCloud; status code: "
						+ statusCode;
				LOGGER.error(message);
				throw new DownloadFailedException(message);
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
	
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@SuppressWarnings("serial")
	static class NotFoundException extends Exception {
		public NotFoundException() {
			super();
		}
		
		public NotFoundException(String arg0) {
			super(arg0);
		}
	}
	
	@ExceptionHandler(value=Exception.class)
	public void handleGenericException(Exception e) {
		LOGGER.error("Error in application controller.", e);
	}
}
