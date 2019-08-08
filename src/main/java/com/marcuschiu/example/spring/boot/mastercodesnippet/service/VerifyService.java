package com.marcuschiu.example.spring.boot.mastercodesnippet.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Stream;

public class VerifyService {

    public static Boolean verify(String outputDirectoryPath) throws Exception {
        SortedMap<String, String> map = new TreeMap<>();

        File directory = new File(outputDirectoryPath);
        File[] files = directory.listFiles();
        for (File file : files) {
            if (!file.getName().equals(".gitkeep")) {
                try (Stream<String> stream = Files.lines(Paths.get(file.getCanonicalPath()))) {
                    stream.forEach((String line) -> {
                        String[] numbers = line.split(" ");
                        map.put(numbers[0], numbers[1]);
                    });
                }
            }
        }

        String previousExitValue = "0";
        for(Map.Entry<String,String> entry : map.entrySet()) {
            if (previousExitValue.compareTo(entry.getKey()) > 0) {
                return false;
            }
            previousExitValue = entry.getValue();
        }

        return true;
    }

    public static Double getThroughput_NumCSsOverSeconds(String outputDirectoryPath) throws IOException {
        SortedMap<String, String> map = new TreeMap<>();

        File directory = new File(outputDirectoryPath);
        File[] files = directory.listFiles();
        for (File file : files) {
            if (!file.getName().equals(".gitkeep")) {
                try (Stream<String> stream = Files.lines(Paths.get(file.getCanonicalPath()))) {
                    stream.forEach((String line) -> {
                        String[] numbers = line.split(" ");
                        map.put(numbers[0], numbers[1]);
                    });
                }
            }
        }

        Integer size = map.size();
        Object[] array = map.entrySet().toArray();
        Double startTime = new Double((String)((Map.Entry)array[0]).getKey());
        Double endTime = new Double((String)((Map.Entry)array[size - 1]).getValue());
        return (size / (endTime - startTime)) * 1000d;
    }

    public static Double getResponseTimeAverageInSeconds(String outputDirectoryPath) throws Exception {
        ArrayList<Double> responseTimes = new ArrayList<>();

        File directory = new File(outputDirectoryPath);
        File[] files = directory.listFiles();
        for (File file : files) {
            if (!file.getName().equals(".gitkeep")) {
                try (Stream<String> stream = Files.lines(Paths.get(file.getCanonicalPath()))) {
                    stream.forEach((String line) -> responseTimes.add(new Double(line)));
                }
            }
        }

        Double sum = 0d;
        for (Double rt : responseTimes) {
            sum += rt;
        }

        return (sum / responseTimes.size()) / 1000d;
    }
}
