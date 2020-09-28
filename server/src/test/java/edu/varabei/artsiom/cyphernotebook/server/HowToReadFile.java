package edu.varabei.artsiom.cyphernotebook.server;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.Key;

public class HowToReadFile {

    private static final Charset charset = StandardCharsets.UTF_8;
    private static final String aesKeyString = "1234567812345678";

    @Test
    public void readFile() {
        final InputStream inputStream = inputStreamFromFileForRead("src/test/resources/hello-world.txt");
        final String content = textFromFile(inputStream);
        Assertions.assertEquals("line1: hello worldline2: Hello World", content);
    }

    @Test
    @SneakyThrows
    public void copyFile() {
        try (InputStream inputStream = inputStreamFromFileForRead("src/test/resources/hello-world.txt");
             OutputStream outputStream = outputStreamForWrite("src/test/resources/hello-world-copy.txt")) {
            inputStream.transferTo(outputStream);
        }
    }

    @Test
    @SneakyThrows
    public void encryptDecryptFile() {
        try (InputStream inputStream = inputStreamFromFileForRead("src/test/resources/hello-world.txt");
             OutputStream outputStream = outputStreamForWrite("src/test/resources/hello-world-encrypted.txt")) {
            InputStream encrypted = new CipherInputStream(inputStream, getAESCipher(Cipher.ENCRYPT_MODE));
            encrypted.transferTo(outputStream);
        }

        try (InputStream inputStream = inputStreamFromFileForRead("src/test/resources/hello-world-encrypted.txt");
             OutputStream outputStream = outputStreamForWrite("src/test/resources/hello-world-decrypted.txt")) {
            OutputStream decrypted = new CipherOutputStream(outputStream, getAESCipher(Cipher.DECRYPT_MODE));
            inputStream.transferTo(decrypted);
        }

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
        final InputStreamReader inputStreamReader = new InputStreamReader(is);
        final BufferedReader reader = new BufferedReader(inputStreamReader);

        StringBuilder content = new StringBuilder();
        String str = "";
        while ((str = reader.readLine()) != null) {
            content.append(str);
        }

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

}
