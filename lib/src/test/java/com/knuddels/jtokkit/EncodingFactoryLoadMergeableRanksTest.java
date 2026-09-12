package com.knuddels.jtokkit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.jupiter.api.Test;

class EncodingFactoryLoadMergeableRanksTest {

    private static final String CL100K_RESOURCE = "/com/knuddels/jtokkit/cl100k_base.tiktoken";

    @Test
    void loadMergeableRanksFromChunkyInputStreamMatchesDirectLoad() throws IOException {
        Map<byte[], Integer> direct = EncodingFactory.loadMergeableRanks(CL100K_RESOURCE);

        try (InputStream in = EncodingFactory.class.getResourceAsStream(CL100K_RESOURCE)) {
            Map<byte[], Integer> fromChunkyStream = EncodingFactory.loadMergeableRanks(new ChunkyInputStream(in), CL100K_RESOURCE);
            assertEquals(direct.size(), fromChunkyStream.size());
        }
    }

    @Test
    void parseMergeableRanksRejectsSplicedRankLineWithLineNumber() {
        String splicedLine = "x 84A= 1520";
        byte[] contents = (splicedLine + "\n").getBytes(StandardCharsets.UTF_8);

        IllegalStateException error = assertThrows(
                IllegalStateException.class,
                () -> EncodingFactory.parseMergeableRanks(contents, "test.tiktoken")
        );

        assertTrue(error.getMessage().contains("line 1"));
        assertTrue(error.getMessage().contains(splicedLine));
        assertTrue(error.getMessage().contains("invalid rank"));
    }

    /**
     * Forces single-byte reads so parsers that rely on stream chunk boundaries see partial buffers.
     */
    private static final class ChunkyInputStream extends InputStream {
        private final InputStream delegate;

        ChunkyInputStream(InputStream delegate) {
            this.delegate = delegate;
        }

        @Override
        public int read() throws IOException {
            return delegate.read();
        }

        @Override
        public int read(byte[] buffer, int offset, int length) throws IOException {
            if (length <= 0) {
                return delegate.read(buffer, offset, length);
            }
            return delegate.read(buffer, offset, 1);
        }
    }
}
