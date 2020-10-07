package edu.varabei.artsiom.cyphernotebook.server;

import edu.varabei.artsiom.cyphernotebook.server.crypto.CryptoUtil;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.io.input.CountingInputStream;
import org.apache.commons.io.output.CountingOutputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.Key;
import java.security.SecureRandom;

public class HowToReadFile {

    private static final Charset charset = StandardCharsets.US_ASCII;
    private static final String aesKeyString = "1234567812345678";

    @Test
    public void readFile() {
        final InputStream inputStream = inputStreamFromFileForRead("src/test/resources/hello-world.txt");
        final String content = textFromFile(inputStream);
        Assertions.assertEquals("line1: hello worldline2: Hello World", content);
    }

//    @Test
//    @SneakyThrows
//    public void encryptDecryptFileWithOutputStream() {
//        try (InputStream rawInput = inputStreamFromFileForRead("src/test/resources/hello-world.txt");
//             OutputStream outputStream = outputStreamForWrite("src/test/resources/hello-world-encrypted-out.txt")) {
//            OutputStream encryptingOutput = new CipherOutputStream(outputStream, getAESCipher(Cipher.ENCRYPT_MODE));
//            transferTo(rawInput, encryptingOutput);
//        }
//
//        try (InputStream encInput = inputStreamFromFileForRead("src/test/resources/hello-world-encrypted-out.txt");
//             OutputStream outputStream = outputStreamForWrite("src/test/resources/hello-world-decrypted-out.txt")) {
//            OutputStream decryptingOutput = new CipherOutputStream(outputStream, getAESCipher(Cipher.DECRYPT_MODE));
//            transferTo(encInput, decryptingOutput);
//        }
//
//        final InputStream inputStream = inputStreamFromFileForRead("src/test/resources/hello-world-encrypted-out.txt");
//        final CipherInputStream decryptingInputStream = new CipherInputStream(inputStream, getAESCipher(Cipher.DECRYPT_MODE));
//        final String content = textFromFile(decryptingInputStream);
//        System.out.println(content);
//    }

    @Test
    @SneakyThrows
    public void encryptDecryptFileWithInputStream() {
        try (InputStream rawInput = inputStreamFromFileForRead("src/test/resources/hello-world.txt");
             OutputStream outputStream = outputStreamForWrite("src/test/resources/hello-world-encrypted-in.txt")) {
            InputStream encryptingInput = new CipherInputStream(rawInput, getAESCipher(Cipher.ENCRYPT_MODE));
            transferTo(encryptingInput, outputStream);
        }

        try (InputStream encInput = inputStreamFromFileForRead("src/test/resources/hello-world-encrypted-in.txt");
             OutputStream outputStream = outputStreamForWrite("src/test/resources/hello-world-decrypted-in.txt")) {
            InputStream decryptingInput = new CipherInputStream(encInput, getAESCipher(Cipher.DECRYPT_MODE));
            transferTo(decryptingInput, outputStream);
        }

        try (InputStream rawInput = inputStreamFromFileForRead("src/test/resources/hello-world.txt");
             InputStream decInput = inputStreamFromFileForRead("src/test/resources/hello-world-decrypted-in.txt")) {
            final String rawContent = textFromFile(rawInput);
            final String decContent = textFromFile(decInput);
            Assertions.assertEquals(rawContent, decContent);
        }
    }

    @Test
    @SneakyThrows
    public void encryptDecryptImageFileWithInputStream() {
        try (InputStream rawInput = inputStreamFromFileForRead("src/test/resources/meem1.jpeg");
             OutputStream outputStream = outputStreamForWrite("src/test/resources/meem1-encrypted.jpeg")) {
            InputStream encryptingInput = new CipherInputStream(rawInput, getAESCipher(Cipher.ENCRYPT_MODE));
            transferTo(encryptingInput, outputStream);
        }

        try (InputStream encInput = inputStreamFromFileForRead("src/test/resources/meem1-encrypted.jpeg");
             OutputStream outputStream = outputStreamForWrite("src/test/resources/meem1-decrypted.jpeg")) {
            InputStream decryptingInput = new CipherInputStream(encInput, getAESCipher(Cipher.DECRYPT_MODE));
            transferTo(decryptingInput, outputStream);
        }

        try (InputStream rawInput = inputStreamFromFileForRead("src/test/resources/meem1.jpeg");
             InputStream decInput = inputStreamFromFileForRead("src/test/resources/meem1-decrypted.jpeg")) {
            Assertions.assertTrue(CryptoUtil.isEqual(rawInput, decInput));
        }
    }

    @Test
    @SneakyThrows
    public void hmm() {
        try (InputStream rawInput = inputStreamFromFileForRead("src/test/resources/hello-world.txt");
             OutputStream outputStream = outputStreamForWrite("src/test/resources/hello-world-encrypted-decrypted-input.txt")) {
            InputStream encryptingInput = new CipherInputStream(rawInput, getAESCipher(Cipher.ENCRYPT_MODE));
            InputStream decryptingInput = new CipherInputStream(encryptingInput, getAESCipher(Cipher.DECRYPT_MODE));
            transferTo(decryptingInput, outputStream);
        }

        try (InputStream rawInput = inputStreamFromFileForRead("src/test/resources/hello-world.txt");
             OutputStream outputStream = outputStreamForWrite("src/test/resources/hello-world-encrypted-decrypted-output.txt")) {
            OutputStream encryptingOutput = new CipherOutputStream(outputStream, getAESCipher(Cipher.ENCRYPT_MODE));
            OutputStream decryptingOutput = new CipherOutputStream(encryptingOutput, getAESCipher(Cipher.DECRYPT_MODE));
            transferTo(rawInput, decryptingOutput);
        }
    }

    @SneakyThrows
    void transferTo(InputStream is, OutputStream os) {
        final CountingInputStream cis = new CountingInputStream(is);
        final CountingOutputStream cos = new CountingOutputStream(os);

        final long transfered = cis.transferTo(cos);

        System.out.printf("\ntransfered: %s\nis: %s\nos: %s\n", transfered, cis.getCount(), cos.getCount());
    }

    @SneakyThrows
    InputStream inputStreamFromFileForRead(String pathToFile) {
        return Files.newInputStream(Paths.get(pathToFile), StandardOpenOption.READ);
    }

    @SneakyThrows
    OutputStream outputStreamForWrite(String pathToFile) {
        return Files.newOutputStream(Paths.get(pathToFile), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
    }

    @SneakyThrows
    String textFromFile(InputStream is) {
        is = new CountingInputStream(is);
        final InputStreamReader inputStreamReader = new InputStreamReader(is);
        final BufferedReader reader = new BufferedReader(inputStreamReader);

        StringBuilder content = new StringBuilder();
        String str;
        while ((str = reader.readLine()) != null) {
            content.append(str);
        }

        System.out.printf("read %s bytes\n", ((CountingInputStream) is).getCount());

        reader.close();

        return content.toString();
    }

    @SneakyThrows
    Cipher getAESCipher(int mode) {
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        Key key = new SecretKeySpec(aesKeyString.getBytes(charset), "AES");
        // required to decrypt
        final IvParameterSpec iv = new IvParameterSpec(aesKeyString.getBytes(charset));
        cipher.init(mode, key, iv);
        return cipher;
    }

    @Test
    @SneakyThrows
    public void lols_pic() {
        val skipped = 128;
        for (int i = 1; i < 1046; i++) {
            String src = String.format("/Users/artemvrby/Downloads/pics/%sp.bmp", i);
            String enc = String.format("src/test/resources/test/cbc/%s-enc.bmp",i );
            String dec = String.format("src/test/resources/test/cbc/%s-dec.bmp",i );
            try {
                try (InputStream rawInput = inputStreamFromFileForRead(src);
                     OutputStream outputStream = outputStreamForWrite(enc)) {
                    val buffer = ByteBuffer.allocate(skipped);
                    rawInput.readNBytes(buffer.array(), 0, skipped);
                    val encryptingInput = new CipherInputStream(rawInput, getAESCipher(Cipher.ENCRYPT_MODE));
                    val lol = new SequenceInputStream(new ByteArrayInputStream(buffer.array()), encryptingInput);
                    System.out.println(lol.transferTo(outputStream));
                }

                try (InputStream encInput = inputStreamFromFileForRead(enc);
                     OutputStream outputStream = outputStreamForWrite(dec)) {
                    val buffer = ByteBuffer.allocate(skipped);
                    encInput.readNBytes(buffer.array(), 0, skipped);
                    val decryptingInput = new CipherInputStream(encInput, getAESCipher(Cipher.DECRYPT_MODE));
                    val decryptedBytes = decryptingInput.readAllBytes();
                    val lol = new SequenceInputStream(new ByteArrayInputStream(buffer.array()), new ByteArrayInputStream(decryptedBytes));
                    lol.transferTo(outputStream);
                }
            } catch (Exception err) {
                System.out.println("failed with " + i);
//                System.out.println(err);
            }
        }
    }



}
