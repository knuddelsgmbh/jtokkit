package com.knuddels.jtokkit;

import com.knuddels.jtokkit.api.Encoding;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

@State(Scope.Thread)
public abstract class AbstractMultiThreadedBenchmark extends AbstractBenchmark {

	private final int threads;
	private ExecutorService executor;

	public AbstractMultiThreadedBenchmark(final int threads) {
		this.threads = threads;
	}

	@Setup
	public void setup() {
		executor = Executors.newFixedThreadPool(threads);
	}

	@TearDown
	public void tearDown() {
		executor.shutdown();
	}

	@Override
	protected List<List<Integer>> encodeAll(final Encoding encoding, final List<String> fileContents) {
		final var futures = fileContents.stream()
				.map(it -> CompletableFuture.supplyAsync(() -> encoding.encode(it), executor))
				.collect(Collectors.toList());

		CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();

		return futures.stream()
				.map(CompletableFuture::join)
				.collect(Collectors.toList());
	}
}
