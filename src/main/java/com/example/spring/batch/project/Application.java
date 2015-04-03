package com.example.spring.batch.project;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

/**
 * Application entry point.
 * 
 * @author Borja López Altarriba
 */

@ComponentScan
@EnableAutoConfiguration
public class Application 
{
	@Autowired
    private JobLauncher jobLauncher;
    
    @Autowired
    private Job job;
    
    @Autowired
    private Environment env;
    
    public static void main(String[] args) {
        final SpringApplication app = new SpringApplication(Application.class);
        app.setWebEnvironment(false);
        app.run(args);
    }
}
