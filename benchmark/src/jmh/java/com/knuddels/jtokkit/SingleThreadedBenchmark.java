package com.knuddels.jtokkit;

import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.IntArrayList;
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

    @Benchmark
    public int benchmarkCl100kBaseTokenCountOrdinary(BenchmarkingState state) {
        var result = 0;
        var encoding = state.cl100kBase;
        for (var fileContent : state.fileContents) {
            result += encoding.countTokensOrdinary(fileContent);
        }
        return result;
    }

    @Override
    protected List<IntArrayList> encodeAll(Encoding encoding, List<String> fileContents) {
        return fileContents.stream()
                .map(encoding::encode)
                .toList();
    }
}
