package edu.varabei.artsiom.cyphernotebook.clientbackend.backend;

import edu.varabei.artsiom.cyphernotebook.clientbackend.StateStore;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest
class BackendWebClientTest {

    @Autowired
    BackendWebClient backend;

    @Autowired
    StateStore stateStore;

    @Test
    public void keygen() {
        backend.saveCredentials("artem", "password");
        backend.genKeyPair();
        backend.getSessionKey();
        System.out.println(stateStore);
    }

    @Test
    @SneakyThrows
    public void uploadFile() {
        backend.saveCredentials("artem", "password");
        backend.genKeyPair();
        backend.getSessionKey();

        val rawContent = Files.newInputStream(Paths.get("my-files/source.jpeg"));
        val feedback = backend.uploadFile(rawContent, "uploaded.jpeg");
        System.out.println(feedback);
    }

}