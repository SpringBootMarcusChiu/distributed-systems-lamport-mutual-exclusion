package com.marcuschiu.example.spring.boot.mastercodesnippet;

import com.marcuschiu.example.spring.boot.mastercodesnippet.configuration.Config;
import com.marcuschiu.example.spring.boot.mastercodesnippet.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;

/**
 * Created by marcus.chiu on 10/1/16.
 *
 * @SpringBootApplication - a convenience annotation that adds all the following:
 * 1. @Config - tags the class as a source of bean definitions
 * 2. @EnableAutoConfiguration - tells Spring Boot to markerMessageReceived adding beans
 * based on classpath settings, other beans, and various property settings
 * 3. @EnableWebMvc - normally added for a Spring MVC app, but Spring boot adds
 * it automatically when it sees 'spring-webmvc' on the classpath.
 * This flags application as a web application and activates key behaviors
 * like setting up DispatcherServlet
 * 4. @ComponentScan - tells Spring to look for other components, configurations,
 * and services in the package this class belongs to, allowing it to find the controllers
 */
@EnableAsync
@SpringBootApplication
public class SameerApplication implements CommandLineRunner {

	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("config/").listFiles()[0];
		Config config = new Config(file);

		// listen on port based on command-line argument (node.id)
		for(String arg : args) {
			if (arg.contains("node.id")) {
				int nodeID = Integer.parseInt(arg.split("=")[1]);

				SpringApplication app = new SpringApplication(SameerApplication.class);
				app.setAddCommandLineProperties(true);
				app.setDefaultProperties(Collections.singletonMap("server.port",
						config.getConfigNodeInfos().get(nodeID).getPort()));
				app.run(args);
				break;
			}
		}
	}

    @Bean
    public Config config() throws FileNotFoundException {
        File file = new File("config/").listFiles()[0];
        return new Config(file);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    Config config;

    @Autowired
    EventService eventService;

    @Value("${node.id}")
    Integer nodeID;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public void run(String... strings) throws Exception {
        if (nodeID.equals(0)) {
            System.out.println("\n\nTHIS NODE IS ZERO");
            System.out.println("press any key to start");
            System.in.read();

            // broadcast START messages (except itself)
            for (int i = 0; i < config.getNumNodes(); i++) {
                if (i != nodeID) {
                    restTemplate.getForObject(
                            config.getConfigNodeInfos().get(i).getNodeURL() + "/message/start",
                            String.class);
                }
            }

            // start it self
            start();
        }
    }

    @Async
    public void start() throws Exception {
        for (int i = 0; i < config.getNumRequests(); i++) {
            eventService.cs_enter();
            System.out.println("CRITICAL SECTION - entered - ID:" + nodeID.toString() + " system-time: " + System.currentTimeMillis());

            Thread.sleep(random(config.getCsExecutionTime()));

            System.out.println("CRITICAL SECTION - leaving - ID:" + nodeID.toString() + " system-time: " + System.currentTimeMillis());
            eventService.cs_leave();

            Thread.sleep(random(config.getInterRequestDelay()));
        }
    }

    private Integer random(Integer dORc) {
        // TODO - expontential something? iono
        return dORc;
    }
}
