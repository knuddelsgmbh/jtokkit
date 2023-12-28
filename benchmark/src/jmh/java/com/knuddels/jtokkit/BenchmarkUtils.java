package com.knuddels.jtokkit;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.*;

public class BenchmarkUtils {

    private static final Charset FILE_CHARSET = StandardCharsets.UTF_8;

    public static void main(String[] args) throws IOException {
        var folder = args.length > 0 ? args[0] : null;
        if (folder == null) {
            System.out.println("Please specify the path to the data folder");
            return;
        }

        var files = loadData(folder);
        var totalBytes = files.stream()
                .mapToInt(it -> it.getBytes(FILE_CHARSET).length)
                .sum();
        var totalMegaBytes = totalBytes / 1024.0 / 1024.0;

        System.out.println("Total files: " + files.size());
        System.out.println("Total megabytes: " + totalMegaBytes);
    }

    public static List<String> loadData(String folder) {
        try {
            var folderPath = Paths.get(folder);
            var fileContents = new ArrayList<String>();
            try (var files = walk(folderPath)) {
                files.forEach(file -> {
                    if (isRegularFile(file)) {
                        try {
                            var content = readString(file, FILE_CHARSET);
                            if (!content.isEmpty()) {
                                fileContents.add(content);
                            }
                        } catch (IOException exception) {
                            throw new RuntimeException("Error while reading file at " + file, exception);
                        }
                    }
                });
            }
            return fileContents;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
