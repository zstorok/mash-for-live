package hu.zstorok.mashforlive.controller;

import hu.zstorok.mashforlive.als.ILiveSetBuilder;
import hu.zstorok.mashforlive.als.LiveSet;
import hu.zstorok.mashforlive.als.LiveSetGenerator;
import hu.zstorok.mashforlive.client.echonest.analyze.Analysis;
import hu.zstorok.mashforlive.client.echonest.analyze.EchoNestClient;
import hu.zstorok.mashforlive.client.echonest.upload.UploadResponseWrapper;
import hu.zstorok.mashforlive.client.soundcloud.SoundCloudClient;
import hu.zstorok.mashforlive.client.soundcloud.SoundCloudConstants;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	private static final String ZIP_FILE_EXTENSION = ".zip";
	private static final String ZIP_FILE_NAME_PREFIX = "mash_for_live_";
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
	
	private File tempDirectory;
	
	public void init() {
		tempDirectory = Files.createTempDir();
	}
	
	@RequestMapping(value = "/service/zip/{id}", method = RequestMethod.GET, produces = "application/octet-stream")
	public void getProjectZip(@PathVariable("id") String id, HttpServletResponse response) throws NotFoundException, DownloadFailedException {
		String tempZipFileName = ZIP_FILE_NAME_PREFIX + id + ZIP_FILE_EXTENSION;
		File tempZipFile = new File(tempDirectory, tempZipFileName);
		if (!tempZipFile.exists()) {
			throw new NotFoundException();
		}
		
		response.addHeader("Content-Disposition", "attachment; filename=" + tempZipFileName);
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
		}
	}
	
	@RequestMapping(value = "/service/zip", method = RequestMethod.POST, produces = "application/octet-stream")
	public String generateProjectZip(String soundCloudTrackId) throws NotDownloadableException,
			DownloadFailedException {
		File projectDirectory = Files.createTempDir();

		// get track data from SoundCloud
		JsonNode soundCloudTrack = soundCloudClient.getTrackAsJsonNode(soundCloudTrackId);
		String soundCloudTrackDownloadUrl = soundCloudTrack.get("download_url").asText() + "?consumer_key="
				+ SoundCloudConstants.CONSUMER_KEY;

		// download track from SoundCloud
		File audioFile = downloadAudioFile(projectDirectory, soundCloudTrackDownloadUrl);

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
			String tempZipId = UUID.randomUUID().toString();
			File tempZipFile = new File(tempDirectory, ZIP_FILE_NAME_PREFIX + tempZipId + ZIP_FILE_EXTENSION);
			FileOutputStream tempZipFileOutputStream = new FileOutputStream(tempZipFile);
			zipOutputStream = new ZipOutputStream(tempZipFileOutputStream);
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
	public void handleGenericException() {}
}
