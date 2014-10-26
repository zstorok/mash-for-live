package hu.zstorok.mashforlive;

import hu.zstorok.mashforlive.als.ILiveSetBuilder;
import hu.zstorok.mashforlive.als.LoopingMultiTrackBarClipLiveSetBuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {
    
    @Bean
    public ILiveSetBuilder getLiveSetBuilder() {
		return new LoopingMultiTrackBarClipLiveSetBuilder();
    }
    
	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
