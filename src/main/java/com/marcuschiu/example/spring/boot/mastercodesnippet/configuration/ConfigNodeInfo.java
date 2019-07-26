package com.marcuschiu.example.spring.boot.mastercodesnippet.configuration;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ConfigNodeInfo {
    Integer id;
    String hostname;
    String port;

    String nodeURL;

    public ConfigNodeInfo(Integer id, String hostname, String port) {
        this.id = id;
        this.hostname = hostname;
        this.port = port;

        this.nodeURL = "http://" + this.hostname + ":" + this.port;
    }
}
