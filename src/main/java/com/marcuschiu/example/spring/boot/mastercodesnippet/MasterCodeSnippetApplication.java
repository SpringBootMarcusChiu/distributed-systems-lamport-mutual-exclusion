package com.marcuschiu.example.spring.boot.mastercodesnippet;

import com.marcuschiu.example.spring.boot.mastercodesnippet.configuration.Configuration;
import com.marcuschiu.example.spring.boot.mastercodesnippet.service.MAPService;
import com.marcuschiu.example.spring.boot.mastercodesnippet.service.SnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;

/**
 * Created by marcus.chiu on 10/1/16.
 * @SpringBootApplication - a convenience annotation that adds all the following:
 *     1. @Configuration - tags the class as a source of bean definitions
 *     2. @EnableAutoConfiguration - tells Spring Boot to start adding beans
 *          based on classpath settings, other beans, and various property settings
 *     3. @EnableWebMvc - normally added for a Spring MVC app, but Spring boot adds
 *          it automatically when it sees 'spring-webmvc' on the classpath.
 *          This flags application as a web application and activates key behaviors
 *          like setting up DispatcherServlet
 *     4. @ComponentScan - tells Spring to look for other components, configurations,
 *          and services in the package this class belongs to, allowing it to find the controllers
 */
@SpringBootApplication
public class MasterCodeSnippetApplication implements CommandLineRunner {

	public static void main(String[] args) throws FileNotFoundException {
		File file = ResourceUtils.getFile("classpath:configuration.txt");
		Configuration configuration = new Configuration(file);

		for(String arg : args) {
			if (arg.contains("node.id")) {
				Integer nodeID = Integer.parseInt(arg.split("=")[1]);

				SpringApplication app = new SpringApplication(MasterCodeSnippetApplication.class);
				app.setAddCommandLineProperties(true);
				app.setDefaultProperties(Collections
						.singletonMap("server.port", configuration.getConfigurationNodeInfos().get(nodeID).getPort()));
				app.run(args);
				break;
			}
		}
	}

	@Value("${node.id}")
	Integer nodeID;

	@Autowired
	MAPService mapService;

	@Autowired
	SnapshotService snapshotService;

	@Bean
	public Configuration configuration() throws FileNotFoundException {
		File file = ResourceUtils.getFile("classpath:configuration.txt");
		return new Configuration(file);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Override
	public void run(String... strings) throws Exception {
		if (this.nodeID == 0) {
			System.out.println("This is Node 0");
			System.out.println("press any key to start");
			System.in.read();
			this.mapService.start();
			//this.snapshotService.start();
		}
	}
}
