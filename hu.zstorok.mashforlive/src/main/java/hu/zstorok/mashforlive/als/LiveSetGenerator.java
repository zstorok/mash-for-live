package hu.zstorok.mashforlive.als;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.zip.GZIPOutputStream;

import org.springframework.stereotype.Service;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.io.Files;

/**
 * Service to generate an Ableton Live set XML file.
 * 
 * @author zstorok
 */
@Service
public class LiveSetGenerator {

	private static final String ALS_TEMPLATE_FILE_PATH = "als_template.xml";
	
	private final MustacheFactory mustacheFactory = new DefaultMustacheFactory();

	/**
	 * Generates and returns the Ableton Live set file as an XML string.
	 * 
	 * @param liveSet the model of the Ableton Live set
	 * @return the Ableton Live set in XML format
	 */
	public String generateXml(LiveSet liveSet) {
		Mustache mustache = mustacheFactory.compile(ALS_TEMPLATE_FILE_PATH);
		try {
			StringWriter stringWriter = new StringWriter();
			mustache.execute(stringWriter, liveSet).flush();
			return stringWriter.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Generates and returns the Ableton Live set file as a GZIP byte array.
	 * 
	 * @param liveSet the model of the Ableton Live set
	 * @return the Ableton Live set as a GZIP byte array
	 */	
	public byte[] generateAls(LiveSet liveSet) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			GZIPOutputStream zipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
			zipOutputStream.write(generateXml(liveSet).getBytes());
			zipOutputStream.finish();
			zipOutputStream.close();
			return byteArrayOutputStream.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				byteArrayOutputStream.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}

	public static void main(String[] args) throws IOException {
		LiveSetGenerator liveSetGenerator = new LiveSetGenerator();
		LiveTrack track1 = new LiveTrack(1, "track1");
		track1.getClips().add(new LiveClip(1, "clip1", "test.mp3", 1, 2, Arrays.asList()));
		track1.getClips().add(new LiveClip(2, "clip2", "test.mp3", 3, 4, Arrays.asList()));
		LiveSet liveSet = new LiveSet(120);
		liveSet.getTracks().add(track1);
		byte[] compressedBytes = liveSetGenerator.generateAls(liveSet);
		Files.write(compressedBytes, new File("output_" + System.currentTimeMillis() + ".als"));
	}
}
