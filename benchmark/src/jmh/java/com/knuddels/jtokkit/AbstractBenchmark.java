package com.knuddels.jtokkit;

import com.knuddels.jtokkit.api.Encoding;
import java.util.List;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public abstract class AbstractBenchmark {

	@Benchmark
	public void benchmarkP50kBase(final BenchmarkingState state, final Blackhole blackhole) {
		blackhole.consume(encodeAll(state.p50kBase, state.fileContents));
	}

	@Benchmark
	public void benchmarkR50kBase(final BenchmarkingState state, final Blackhole blackhole) {
		blackhole.consume(encodeAll(state.r50kBase, state.fileContents));
	}

	@Benchmark
	public void benchmarkP50kEdit(final BenchmarkingState state, final Blackhole blackhole) {
		blackhole.consume(encodeAll(state.p50kEdit, state.fileContents));
	}

	@Benchmark
	public void benchmarkCl100kBase(final BenchmarkingState state, final Blackhole blackhole) {
		blackhole.consume(encodeAll(state.cl100kBase, state.fileContents));
	}

	/**
	 * Encodes all file contents with the given encoding.
	 *
	 * @param encoding the encoding to use
	 * @param fileContents the file contents to encode
	 * @return a list of encoded token lists
	 */
	protected abstract List<List<Integer>> encodeAll(final Encoding encoding, final List<String> fileContents);
}
