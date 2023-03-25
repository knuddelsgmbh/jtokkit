package com.knuddels.jtokkit;

import com.knuddels.jtokkit.api.Encoding;
import java.io.IOException;
import java.util.List;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class BenchmarkingState {
	public List<String> fileContents;

	@Param("data")
	public String dataFolderPath;

	public final Encoding cl100kBase = EncodingFactory.cl100kBase();
	public final Encoding p50kBase = EncodingFactory.p50kBase();
	public final Encoding p50kEdit = EncodingFactory.p50kEdit();
	public final Encoding r50kBase = EncodingFactory.r50kBase();

	@Setup()
	public void setup() throws IOException {
		fileContents = BenchmarkUtils.loadData(dataFolderPath);
	}
}
