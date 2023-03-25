package com.knuddels.jtokkit;

import com.knuddels.jtokkit.api.Encoding;
import java.util.List;
import java.util.stream.Collectors;

public class SingleThreadedBenchmark extends AbstractBenchmark {

	@Override
	protected List<List<Integer>> encodeAll(final Encoding encoding, final List<String> fileContents) {
		return fileContents.stream()
				.map(encoding::encode)
				.collect(Collectors.toList());
	}
}
