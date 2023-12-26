package com.knuddels.jtokkit;


import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public class Cl100kParserBenchmark {
    @Benchmark
    public void benchmarkIsLetter(BenchmarkingState state, Blackhole bh) {
        for (var fileContent : state.fileContents) {
            fileContent.codePoints().forEachOrdered(cp -> bh.consume(Cl100kParser.isLetter(cp)));
        }
    }

    @Benchmark
    public void benchmarkIsLetterOrNumeric(BenchmarkingState state, Blackhole bh) {
        for (var fileContent : state.fileContents) {
            fileContent.codePoints().forEachOrdered(cp -> bh.consume(Cl100kParser.isLetterOrNumeric(cp)));
        }
    }

    @Benchmark
    public void benchmarkIsNewline(BenchmarkingState state, Blackhole bh) {
        for (var fileContent : state.fileContents) {
            fileContent.codePoints().forEachOrdered(cp -> bh.consume(Cl100kParser.isNewline(cp)));
        }
    }

    @Benchmark
    public void benchmarkIsNotNewlineOrLetterOrNumeric(BenchmarkingState state, Blackhole bh) {
        for (var fileContent : state.fileContents) {
            fileContent.codePoints().forEachOrdered(cp -> bh.consume(Cl100kParser.isNotNewlineOrLetterOrNumeric(cp)));
        }
    }

    @Benchmark
    public void benchmarkIsNotWhitespaceOrLetterOrNumeric(BenchmarkingState state, Blackhole bh) {
        for (var fileContent : state.fileContents) {
            fileContent.codePoints().forEachOrdered(cp -> bh.consume(Cl100kParser.isNotWhitespaceOrLetterOrNumeric(cp)));
        }
    }

    @Benchmark
    public void benchmarkIsNumeric(BenchmarkingState state, Blackhole bh) {
        for (var fileContent : state.fileContents) {
            fileContent.codePoints().forEachOrdered(cp -> bh.consume(Cl100kParser.isNumeric(cp)));
        }
    }

    @Benchmark
    public void benchmarkIsWhitespace(BenchmarkingState state, Blackhole bh) {
        for (var fileContent : state.fileContents) {
            fileContent.codePoints().forEachOrdered(cp -> bh.consume(Cl100kParser.isWhitespace(cp)));
        }
    }

    @Benchmark
    public void benchmarkToUtf8Conversion(BenchmarkingState state, Blackhole bh) {
        var dst = new ByteArrayList();
        for (var fileContent : state.fileContents) {
            bh.consume(Cl100kParser.addUtf8Bytes(fileContent, 0, fileContent.length(), dst));
        }
    }
}
