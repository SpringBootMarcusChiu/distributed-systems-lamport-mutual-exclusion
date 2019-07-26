package com.marcuschiu.example.spring.boot.mastercodesnippet.service;

import com.marcuschiu.example.spring.boot.mastercodesnippet.configuration.Config;
import com.marcuschiu.example.spring.boot.mastercodesnippet.model.RequestMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.PriorityQueue;

@Service
public class LamportService {

    // @Autowired - auto fills (Config object created within SameerApplication.java)
    @Autowired
    Config config;

    // @Autowired - auto fills (RestTemplate created within SameerApplication.java)
    @Autowired
    RestTemplate restTemplate;

    // @Value - auto fills based on command-line argument 'node.id' value
    @Value("${node.id}")
    Integer nodeID;

    Integer lamportTimestamp = 0;
    Integer numRepliesReceived = 0;
    PriorityQueue<RequestMessage> requestQueue = new PriorityQueue<>();

    public void cs_enter() {

    }

    public void cs_leave() {

    }

    // Example use of RestTemplate
    private void exampleRestTemplateUSE() {
        String endpoint = config.getConfigNodeInfos().get(0).getNodeURL() + "/message/request";

        String response = restTemplate.postForObject(
                endpoint,
                new RequestMessage(lamportTimestamp, nodeID),
                String.class);
    }
}
