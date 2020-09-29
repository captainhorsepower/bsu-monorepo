package edu.varabei.artsiom.cyphernotebook.clientbackend.crypto;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.nio.ByteBuffer;
import java.security.Key;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AESCryptoService {

    // 16 bytes ~ 128 bit ~ AES block size
    private static final int BYTE_BLOCK_LEN = 16;

    public InputStream encrypt(InputStream rawContentStream, String transformation, String base64Key) {
        val key = secretKey(base64Key);
        val prependedBlock = new ByteArrayInputStream(secureRandom128Bit());
        val prependedStream = new SequenceInputStream(prependedBlock, rawContentStream);
        return new CipherInputStream(prependedStream, getCipher(Cipher.ENCRYPT_MODE, transformation, key));
    }

    @SneakyThrows
    public InputStream decrypt(InputStream encryptedContentStream, String transformation, String base64Key) {
        val key = secretKey(base64Key);
        val decryptingStream = new CipherInputStream(encryptedContentStream,
                getCipher(Cipher.DECRYPT_MODE, transformation, key));
        decryptingStream.skipNBytes(BYTE_BLOCK_LEN);
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

    @SneakyThrows
    Key secretKey(String base64) {
        return new SecretKeySpec(Base64.decodeBase64(base64), "AES");
    }

}
