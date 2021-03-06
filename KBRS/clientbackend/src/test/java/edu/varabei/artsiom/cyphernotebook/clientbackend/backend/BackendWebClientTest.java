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

        backend.uploadFile(Files.newInputStream(Paths.get("my-files/textfile.txt")), "text.txt");
    }

    @Test
    @SneakyThrows
    public void getFile() {
        backend.saveCredentials("artem", "password");
        backend.genKeyPair();
        backend.getSessionKey();

        val file = Files.newOutputStream(Paths.get("my-files/downloaded.jpeg"));
        final Number bytesRead = backend.getFile(file, "meem1-encrypted.jpeg");
        System.out.println("read " + bytesRead.longValue() + " bytes");
    }

    @Test
    public void deleteFile() {
        backend.saveCredentials("artem", "password");
        backend.genKeyPair();
        backend.getSessionKey();

        val response = backend.deleteFile("deleteme");
        System.out.println(response);
    }

    @Test
    @SneakyThrows
    public void uploadAndDownloadPPTX() {
        backend.saveCredentials("artem", "password");
        backend.genKeyPair();
        backend.getSessionKey();

        val folder = "folder/aircloud.backup";

        backend.uploadFile(
                Files.newInputStream(Paths.get("my-files/aircloud.pptx")),
                folder);
        backend.getSessionKey();
        backend.getFile(
                Files.newOutputStream(Paths.get("my-files/aircloud-down.pptx")),
                folder);
    }

}