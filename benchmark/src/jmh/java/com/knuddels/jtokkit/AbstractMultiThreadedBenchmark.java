package com.knuddels.jtokkit;

import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.IntArrayList;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@State(Scope.Thread)
public abstract class AbstractMultiThreadedBenchmark extends AbstractBenchmark {

    private final int threads;
    private ExecutorService executor;

    public AbstractMultiThreadedBenchmark(int threads) {
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
    protected List<IntArrayList> encodeAll(Encoding encoding, List<String> fileContents) {
        var futures = fileContents.stream()
                .map(it -> CompletableFuture.supplyAsync(() -> encoding.encode(it), executor))
                .toList();

        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();

        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }
}
