package com.marcuschiu.example.spring.boot.mastercodesnippet.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    public static void main(String args[]) {
        String one = "1234567891";
        String two = "1234567890";
        int t = one.compareTo(two);
        System.out.println(t);
    }
}
