package hu.zstorok.mashforlive;

import hu.zstorok.mashforlive.als.ILiveSetBuilder;
import hu.zstorok.mashforlive.als.LiveSetBuilder;
import hu.zstorok.mashforlive.client.EchoNestClient;
import hu.zstorok.mashforlive.client.SoundCloudClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {
    
    @Autowired
    private SoundCloudClient soundCloudClient;
    @Autowired
    private EchoNestClient echoNestClient;

    @Bean
    public ILiveSetBuilder getLiveSetBuilder() {
		return new LiveSetBuilder();
    }
    
	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
