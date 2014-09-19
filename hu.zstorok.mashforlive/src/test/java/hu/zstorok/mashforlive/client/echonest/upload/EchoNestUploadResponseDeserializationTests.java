package hu.zstorok.mashforlive.client.echonest.upload;

import hu.zstorok.mashforlive.Application;
import hu.zstorok.mashforlive.client.echonest.upload.UploadResponseWrapper;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class EchoNestUploadResponseDeserializationTests {

	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void deserializeFromFile() throws JsonProcessingException, IOException {
		InputStream inputStream = getClass().getResourceAsStream("track_upload_sample.txt");
		UploadResponseWrapper uploadResponseWrapper = objectMapper.reader(UploadResponseWrapper.class).readValue(inputStream);
		Assert.assertNotNull(uploadResponseWrapper);
		Assert.assertNotNull(uploadResponseWrapper.getResponse());
	}

}
