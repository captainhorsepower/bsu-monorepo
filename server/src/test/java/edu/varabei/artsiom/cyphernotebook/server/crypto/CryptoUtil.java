package edu.varabei.artsiom.cyphernotebook.server.crypto;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

@UtilityClass
public class CryptoUtil {

    @SneakyThrows
    public boolean isEqual(InputStream i1, InputStream i2) {

        ReadableByteChannel ch1 = Channels.newChannel(i1);
        ReadableByteChannel ch2 = Channels.newChannel(i2);

        ByteBuffer buf1 = ByteBuffer.allocateDirect(1024);
        ByteBuffer buf2 = ByteBuffer.allocateDirect(1024);

        int equalCounter = 0;
        int total1 = 0, total2 = 0;
        try (i1; i2) {
            while (true) {

                int n1 = ch1.read(buf1);
                int n2 = ch2.read(buf2);

                total1 += n1 == -1 ? 0 : n1;
                total2 += n2 == -1 ? 0 : n2;

                if (n1 == -1 && n2 == -1)
                    return total1 == total2;

                buf1.flip();
                buf2.flip();

                final int availableInBoth = Math.min(available(buf1), available(buf2));
                for (int i = 0; i < availableInBoth; i++) {
                    if (buf1.get() != buf2.get()) {
                        System.out.printf("equal %s bytes (not all)\n", equalCounter);
                        return false;
                    }
                    equalCounter++;
                }

                buf1.compact();
                buf2.compact();
            }
        }
    }

    private int available(ByteBuffer buf) {
        return buf.limit() - buf.position();
    }
}
