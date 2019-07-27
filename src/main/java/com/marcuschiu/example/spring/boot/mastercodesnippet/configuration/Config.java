package com.marcuschiu.example.spring.boot.mastercodesnippet.configuration;

import lombok.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

@Data
public class Config {

    String fileName;
    Integer numNodes;
    Integer interRequestDelay;
    Integer csExecutionTime;
    Integer numRequests;
    ArrayList<ConfigNodeInfo> configNodeInfos;

    public Config(File file) throws FileNotFoundException {
        fileName = file.getName().split("\\.")[0];

        ArrayList<String> lines = scrubConfigurationFile(file);

        String[] numbers = lines.get(0).split(" ");
        numNodes = Integer.parseInt(numbers[0]);
        interRequestDelay = Integer.parseInt(numbers[1]);
        csExecutionTime = Integer.parseInt(numbers[2]);
        numRequests = Integer.parseInt(numbers[3]);

        configNodeInfos = new ArrayList<>();
        for (int i = 1; i <= numNodes; i++) {
            String[] nodeInfo = lines.get(i).split(" ");

            ConfigNodeInfo configNodeInfo = new ConfigNodeInfo(
                    Integer.parseInt(nodeInfo[0]),
                    nodeInfo[1],
                    nodeInfo[2]);

            configNodeInfos.add(configNodeInfo);
        }
    }

    /**
     * remove empty lines and comments
     *
     * @param file
     * @return ArrayList<String> list of valid lines
     * @throws FileNotFoundException
     */
    private ArrayList<String> scrubConfigurationFile(File file) throws FileNotFoundException {
        ArrayList<String> configurationLines = new ArrayList<>();

        Scanner sc = new Scanner(file);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();

            // only lines starting with digit are valid
            if (line.length() > 0 && Character.isDigit(line.charAt(0))) {
                // remove comments from line (i.e. # followed by whatever
                int offset = line.indexOf("#");
                if (-1 != offset) {
                    line = line.substring(0, offset);
                }

                configurationLines.add(line);
            }
        }

        return configurationLines;
    }
}
