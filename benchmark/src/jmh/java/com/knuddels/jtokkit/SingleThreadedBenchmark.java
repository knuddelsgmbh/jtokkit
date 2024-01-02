package com.knuddels.jtokkit;

import com.knuddels.jtokkit.api.Encoding;
import org.openjdk.jmh.annotations.Benchmark;

import java.util.List;

public class SingleThreadedBenchmark extends AbstractBenchmark {

    @Benchmark
    public int benchmarkCl100kBaseTokenCount(BenchmarkingState state) {
        var result = 0;
        var encoding = state.cl100kBase;
        for (var fileContent : state.fileContents) {
            result += encoding.countTokens(fileContent);
        }
        return result;
    }

    @Override
    protected List<List<Integer>> encodeAll(final Encoding encoding, final List<String> fileContents) {
        return fileContents.stream()
                .map(encoding::encode)
                .toList();
    }
}
