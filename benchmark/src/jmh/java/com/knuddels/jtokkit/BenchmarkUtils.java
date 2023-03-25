package com.knuddels.jtokkit;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BenchmarkUtils {

	private static final Charset FILE_CHARSET = StandardCharsets.UTF_8;

	public static void main(final String[] args) throws IOException {
		final var folder = args.length > 0 ? args[0] : null;
		if (folder == null) {
			System.out.println("Please specify the path to the data folder");
			return;
		}

		final var files = loadData(folder);
		final var totalBytes = files.stream()
				.mapToInt(it -> it.getBytes(FILE_CHARSET).length)
				.sum();
		final var totalMegaBytes = totalBytes / 1024.0 / 1024.0;

		System.out.println("Total files: " + files.size());
		System.out.println("Total megabytes: " + totalMegaBytes);
	}

	public static List<String> loadData(final String folder) throws IOException {
		final var folderPath = Paths.get(folder);
		final var fileContents = new ArrayList<String>();
		try (var files = Files.walk(folderPath)) {
			files.forEach(file -> {
				if (Files.isRegularFile(file) || file.endsWith(".txt")) {
					try {
						final var content = String.join("\n", Files.readAllLines(file, FILE_CHARSET));
						fileContents.add(content);
					} catch (IOException exception) {
						throw new RuntimeException("Error while reading file at " + file, exception);
					}
				}
			});
		}
		return fileContents;
	}
}
