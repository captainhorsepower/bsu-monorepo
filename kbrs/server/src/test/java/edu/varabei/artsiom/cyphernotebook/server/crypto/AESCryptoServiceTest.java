package edu.varabei.artsiom.cyphernotebook.server.crypto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AESCryptoServiceTest {

    @Autowired
    AESCryptoService AESCryptoService;

    @Autowired
    KeyGenerator aesKeyGenerator;

    @Test
    @SneakyThrows
    public void encryptDecryptBytes() {
        final SecretKey sessionKey = aesKeyGenerator.generateKey();

        ByteBuffer randomBytes = ByteBuffer.allocate(2056);
        final byte[] bytes = randomBytes.array();
        new Random().nextBytes(bytes);

        final InputStream encryptedInput = AESCryptoService.encrypt(new ByteArrayInputStream(bytes), sessionKey);
        final InputStream decryptedInput = AESCryptoService.decrypt(encryptedInput, sessionKey);
        final ByteArrayInputStream rawInput = new ByteArrayInputStream(bytes);

        assertTrue(CryptoUtil.isEqual(rawInput, decryptedInput));
    }

    @Test
    @SneakyThrows
    public void encryptDecryptFile() {
        final SecretKey sessionKey = aesKeyGenerator.generateKey();

        // encrypt
        try (final InputStream rawImage = CryptoUtil.inputStreamFromFileForRead("src/test/resources/meem2.jpeg");
             final OutputStream out = CryptoUtil.outputStreamForWrite("src/test/resources/meem2-enc.jpeg")) {
            final InputStream encrypt = AESCryptoService.encrypt(rawImage, sessionKey);
            encrypt.transferTo(out);
        }
        // decrypt
        try (final InputStream rawImage = CryptoUtil.inputStreamFromFileForRead("src/test/resources/meem2-enc.jpeg");
             final OutputStream out = CryptoUtil.outputStreamForWrite("src/test/resources/meem2-dec.jpeg")) {
            final InputStream decrypt = AESCryptoService.decrypt(rawImage, sessionKey);
            decrypt.transferTo(out);
        }

        // compare
        try (final InputStream rawImage = CryptoUtil.inputStreamFromFileForRead("src/test/resources/meem2.jpeg");
             final InputStream decImage = CryptoUtil.inputStreamFromFileForRead("src/test/resources/meem2-dec.jpeg")) {
            assertTrue(CryptoUtil.isEqual(rawImage, decImage));
        }

    }


}