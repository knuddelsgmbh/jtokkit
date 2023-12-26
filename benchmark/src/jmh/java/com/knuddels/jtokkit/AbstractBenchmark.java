package com.knuddels.jtokkit;

import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.IntArrayList;
import org.openjdk.jmh.annotations.Benchmark;

import java.util.List;

public abstract class AbstractBenchmark {

    @Benchmark
    public Object benchmarkP50kBase(BenchmarkingState state) {
        return encodeAll(state.p50kBase, state.fileContents);
    }

    @Benchmark
    public Object benchmarkR50kBase(BenchmarkingState state) {
        return encodeAll(state.r50kBase, state.fileContents);
    }

    @Benchmark
    public Object benchmarkP50kEdit(BenchmarkingState state) {
        return encodeAll(state.p50kEdit, state.fileContents);
    }

    @Benchmark
    public Object benchmarkCl100kBase(BenchmarkingState state) {
        return encodeAll(state.cl100kBase, state.fileContents);
    }

    /**
     * Encodes all file contents with the given encoding.
     *
     * @param encoding     the encoding to use
     * @param fileContents the file contents to encode
     * @return a list of encoded token lists
     */
    protected abstract List<IntArrayList> encodeAll(Encoding encoding, List<String> fileContents);
}
