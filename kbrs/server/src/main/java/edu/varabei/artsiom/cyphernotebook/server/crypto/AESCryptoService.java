package edu.varabei.artsiom.cyphernotebook.server.crypto;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.nio.ByteBuffer;
import java.security.Key;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AESCryptoService {

    // 16 bytes ~ 128 bit ~ AES block size
    private static final int BYTE_BLOCK_LEN = 16;
    private final String aesTransformation;

    public InputStream encrypt(InputStream rawContentStream, Key sessionKey) {
        final ByteArrayInputStream prependedBlock = new ByteArrayInputStream(secureRandom128Bit());
        final SequenceInputStream prependedStream = new SequenceInputStream(prependedBlock, rawContentStream);
        return new CipherInputStream(prependedStream, getCipher(Cipher.ENCRYPT_MODE, sessionKey));
    }

    @SneakyThrows
    public InputStream decrypt(InputStream encryptedContentStream, Key sessionKey) {
        final CipherInputStream decryptingStream = new CipherInputStream(encryptedContentStream, getCipher(Cipher.DECRYPT_MODE, sessionKey));
        skipNBytes(decryptingStream, BYTE_BLOCK_LEN);
        return decryptingStream;
    }

    // skip N bytes not found on every damn JRE, so here it is (copied from java.io.InputStream)
    public void skipNBytes(InputStream is, long n) throws IOException {
        if (n > 0) {
            long ns = is.skip(n);
            if (ns >= 0 && ns < n) { // skipped too few bytes
                // adjust number to skip
                n -= ns;
                // read until requested number skipped or EOS reached
                while (n > 0 && is.read() != -1) {
                    n--;
                }
                // if not enough skipped, then EOFE
                if (n != 0) {
                    throw new EOFException();
                }
            } else if (ns != n) { // skipped negative or too many bytes
                throw new IOException("Unable to skip exactly");
            }
        }
    }

    @SneakyThrows
    Cipher getCipher(int mode, Key key) {
        final Cipher cipher = Cipher.getInstance(aesTransformation);
        final byte[] iv = secureRandom128Bit();
        cipher.init(mode, key, new IvParameterSpec(iv));
        return cipher;
    }

    byte[] secureRandom128Bit() {
        final ByteBuffer buffer = ByteBuffer.allocate(BYTE_BLOCK_LEN);
        new SecureRandom().nextBytes(buffer.array());
        return buffer.array();
    }

}
