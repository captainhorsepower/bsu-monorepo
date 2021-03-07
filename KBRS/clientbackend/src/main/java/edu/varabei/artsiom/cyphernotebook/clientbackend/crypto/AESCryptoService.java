package edu.varabei.artsiom.cyphernotebook.clientbackend.crypto;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
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

    public InputStream encrypt(InputStream rawContentStream, String transformation, Key key) {
        val prependedBlock = new ByteArrayInputStream(secureRandom128Bit());
        val prependedStream = new SequenceInputStream(prependedBlock, rawContentStream);
        return new CipherInputStream(prependedStream, getCipher(Cipher.ENCRYPT_MODE, transformation, key));
    }

    @SneakyThrows
    public InputStream decrypt(InputStream encryptedContentStream, String transformation, Key key) {
        val decryptingStream = new CipherInputStream(encryptedContentStream, getCipher(Cipher.DECRYPT_MODE, transformation, key));
        skipNBytes(decryptingStream, BYTE_BLOCK_LEN);
        return decryptingStream;
    }

    @SneakyThrows
    Cipher getCipher(int mode, String aesTransformation, Key key) {
        val cipher = Cipher.getInstance(aesTransformation);
        cipher.init(mode, key, new IvParameterSpec(secureRandom128Bit()));
        return cipher;
    }

    byte[] secureRandom128Bit() {
        val buffer = ByteBuffer.allocate(BYTE_BLOCK_LEN);
        new SecureRandom().nextBytes(buffer.array());
        return buffer.array();
    }

    // copy-paste from openjdk, sorry
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

}
