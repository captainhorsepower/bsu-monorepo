package edu.varabei.artsiom.cyphernotebook.clientbackend.backend;

import edu.varabei.artsiom.cyphernotebook.clientbackend.StateStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendWebClientTest {

    @Autowired
    BackendWebClient backend;

    @Autowired
    StateStore stateStore;

    @Test
    public void keygen() {
        // setup
        backend.saveCredentials("artem", "password");
        backend.genKeyPair();
        backend.getSessionKey();
        System.out.println(stateStore);
    }

}