package edu.varabei.artsiom.cyphernotebook.server.crypto;

import jdk.jshell.EvalException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.Key;

@Service
@RequiredArgsConstructor
public class CryptoFilesService {

    private final AESCryptoService aesService;

    @SneakyThrows
    public InputStream getFileEncrypted(String pathToFile, Key sessionKey) {
        val path = Paths.get(pathToFile);
        if (!Files.exists(path)) throw new RuntimeException("File " + pathToFile + " does not exist");

        val is = Files.newInputStream(Paths.get(pathToFile), StandardOpenOption.READ);

        return aesService.encrypt(is, sessionKey);
    }

    @SneakyThrows
    InputStream readFile(String pathToFile) {
        return Files.newInputStream(Paths.get(pathToFile), StandardOpenOption.READ);
    }

    @SneakyThrows
    OutputStream outputStreamForWrite(String pathToFile) {
        return Files.newOutputStream(Paths.get(pathToFile), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
    }

}
