package com.marcuschiu.example.spring.boot.mastercodesnippet.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class FileService {

    @Value("${node.id}")
    Integer nodeID;

    @Value("${application.output.path}")
    String outputPath;

    String outputFileName;

    @PostConstruct
    public void postConstruct() {
        cleanOutputDirectory(outputPath);
        outputFileName = outputPath + "node-" + nodeID.toString() + ".txt";
    }

    private void cleanOutputDirectory(String outputDirectoryPath) {
        File directory = new File(outputDirectoryPath);
        File[] files = directory.listFiles();
        for (File file : files) {
            if (!file.getName().equals(".gitkeep")) {
                file.delete();
            }
        }
    }

    public void writeLine(String line) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName, true))) {
            writer.write(line + "\n");
        }
    }
}
