package hu.zstorok.mashforlive.client.echonest.analyze;

import hu.zstorok.mashforlive.Application;
import hu.zstorok.mashforlive.client.echonest.analyze.Analysis;

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
public class EchoNestAnalysisDeserializationTests {

	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void deserializeFromFile() throws JsonProcessingException, IOException {
		InputStream inputStream = getClass().getResourceAsStream("analysis_sample_v3_3_2.txt");
		Analysis analysis = objectMapper.reader(Analysis.class).readValue(inputStream);
		Assert.assertNotNull(analysis);
		Assert.assertNotNull(analysis.getBars());
		Assert.assertNotNull(analysis.getBeats());
		Assert.assertNotNull(analysis.getMeta());
		Assert.assertNotNull(analysis.getSections());
		Assert.assertNotNull(analysis.getSegments());
		Assert.assertNotNull(analysis.getTatums());
		Assert.assertNotNull(analysis.getTrack());
	}

}
