package edu.varabei.artsiom.cyphernotebook.clientbackend.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class BackendWebClient {

    //TODO 9/30/20: try webClient?
    private final RestTemplate restTemplate;
    private final StateStore stateStore;

    public void keygen(String username, String password) {

    }

}
