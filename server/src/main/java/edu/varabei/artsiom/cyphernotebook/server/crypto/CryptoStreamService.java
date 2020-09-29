package edu.varabei.artsiom.cyphernotebook.server.crypto;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

@Service
@RequiredArgsConstructor
public class CryptoStreamService {

    // 16 bytes ~ 128 bit ~ AES block size
    private static final int BYTE_BLOCK_LEN = 16;
//    private final Charset charset;
    private final String aesTransformation;
//    private final KeyGenerator aesKeyGenerator;

    public InputStream encrypt(InputStream rawContentStream, Key sessionKey) {
        final ByteArrayInputStream prependedBlock = new ByteArrayInputStream(secureRandom128Bit());
        final SequenceInputStream prependedStream = new SequenceInputStream(prependedBlock, rawContentStream);
        return new CipherInputStream(prependedStream, getCipher(Cipher.ENCRYPT_MODE, sessionKey));
    }

    @SneakyThrows
    public InputStream decrypt(InputStream encryptedContentStream, Key sessionKey) {
        final CipherInputStream decryptingStream = new CipherInputStream(encryptedContentStream, getCipher(Cipher.DECRYPT_MODE, sessionKey));
        decryptingStream.skipNBytes(BYTE_BLOCK_LEN);
        return decryptingStream;
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
