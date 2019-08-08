package com.marcuschiu.example.spring.boot.mastercodesnippet;

import com.marcuschiu.example.spring.boot.mastercodesnippet.configuration.Config;
import com.marcuschiu.example.spring.boot.mastercodesnippet.service.EventService;
import com.marcuschiu.example.spring.boot.mastercodesnippet.service.FileService;
import com.marcuschiu.example.spring.boot.mastercodesnippet.service.VerifyService;
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
import java.util.ArrayList;
import java.util.Collections;
import java.lang.Math;
import java.util.List;
import java.util.Random;

@EnableAsync
@SpringBootApplication
public class SameerApplication implements CommandLineRunner {

    public static void main(String[] args) throws Exception {
        File file = new File("config/").listFiles()[0];
        Config config = new Config(file);

        for (String arg : args) {
            // listen on port based on command-line argument (node.id)
            if (arg.contains("node.id")) {
                int nodeID = Integer.parseInt(arg.split("=")[1]);

                SpringApplication app = new SpringApplication(SameerApplication.class);
                app.setAddCommandLineProperties(true);
                app.setDefaultProperties(Collections.singletonMap("server.port",
                        config.getConfigNodeInfos().get(nodeID).getPort()));
                app.run(args);
                break;
            }
            // verify output files are mutual exclusion
            if (arg.contains("verify")) {
                System.out.println("OUTPUT: " + VerifyService.verify("output/cs-times"));
            }
            if (arg.contains("statistic")) {
                System.out.println("RESPONSE TIME AVERAGE (seconds): " + VerifyService.getResponseTimeAverageInSeconds("output/response-times"));
                System.out.println("THROUGHPUT (num cs / seconds): " + VerifyService.getThroughput_NumCSsOverSeconds("output/cs-times"));
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

    @Autowired
    FileService fileService;

    @Value("${node.id}")
    Integer nodeID;

    @Autowired
    RestTemplate restTemplate;

    Random rand = new Random();

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

        ArrayList<Double> responseTimes = new ArrayList<>();
        String requestTime;
        String enterTime;
        String exitTime;

        // system time
        for (int i = 0; i < config.getNumRequests(); i++) {
            requestTime = getSystemTime(0);
            System.out.println("CRITICAL SECTION - request - NODE ID:" + nodeID.toString() + " TIME: " + requestTime);

            eventService.cs_enter();
            enterTime = getSystemTime(0);
            System.out.println("CRITICAL SECTION - entered - NODE ID:" + nodeID.toString() + " TIME: " + enterTime);

            Thread.sleep(exponentialRNG(rand, config.getCsExecutionTime()));

            exitTime = getSystemTime(0);
            System.out.println("CRITICAL SECTION - leaving - NODE ID:" + nodeID.toString() + " TIME: " + exitTime);

            responseTimes.add(new Double(enterTime) - new Double(requestTime));

            fileService.writeCSTimeLine(enterTime + " " + exitTime);
            eventService.cs_leave();

            Thread.sleep(exponentialRNG(rand, config.getInterRequestDelay()));
        }

        fileService.writeResponseTimeAverage(average(responseTimes));
    }

    private Double average(List<Double> numbers) {
        Double sum = 0d;
        for (Double d: numbers) {
            sum += d;
        }

        return sum / numbers.size();
    }

    private long exponentialRNG(Random uniformRNG, double distributionMean) {
        // Applied inversion method described https://stackoverflow.com/questions/2106503/pseudorandom-number-generator-exponential-distribution
        double uniformRandomNumber = uniformRNG.nextDouble();
        double lambda = 1 / distributionMean; // distribution mean is defined by 1/lambda, where lambda is the rate parameter: https://en.wikipedia.org/wiki/Exponential_distribution#Mean,_variance,_moments_and_median
        double exponentialRandomNumber = Math.log(1 - uniformRandomNumber) / (-lambda); // inversion method
        //System.out.println(exponentialRandomNumber);
        return (long) exponentialRandomNumber;
        //return dORc;
    }

    private String getSystemTime(Integer nodeID) {
        return restTemplate.getForObject(
                config.getConfigNodeInfos().get(nodeID).getNodeURL() + "/message/system-time",
                String.class);
    }
}