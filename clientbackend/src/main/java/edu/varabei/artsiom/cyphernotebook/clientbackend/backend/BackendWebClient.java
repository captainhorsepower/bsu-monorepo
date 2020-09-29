package edu.varabei.artsiom.cyphernotebook.clientbackend.backend;

import edu.varabei.artsiom.cyphernotebook.clientbackend.StateStore;
import edu.varabei.artsiom.cyphernotebook.clientbackend.crypto.PubKeyService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;

@Service
@RequiredArgsConstructor
public class BackendWebClient {

    public static final String KEY_PAIR = "key-pair";
    public static final String SESSION_KEY = "session-key";

    //TODO 9/30/20: try webClient?
    private final RestTemplate restTemplate;
    private final StateStore stateStore;
    private final PubKeyService pubKeyService;

    private final String pubKeyTransformation;
    private final String pubKeyAlgorithm;

    public void saveCredentials(String username, String password) {
        stateStore.put("username", username);
        stateStore.put("password", password);
    }

    public void genKeyPair() {
        val keyPair = pubKeyService.genKeyPair();
        stateStore.put(KEY_PAIR, keyPair);
    }

    public void getSessionKey() {
        final KeyPair keyPair = stateStore.get(KEY_PAIR);
        val request = new KeygenRequestDTO(
                pubKeyTransformation,
                pubKeyAlgorithm,
                base64(keyPair.getPublic().getEncoded()));

        val response = restTemplate.exchange("http://localhost:8080/api/keygen",
                HttpMethod.POST, new HttpEntity<>(request, createHeaders()), SessionKeyDTO.class);

        checkCookie(response);

        val sessionKeyDTO = response.getBody();
        val keyHolder = new SessionKeyHolder(
                new SecretKeySpec(base64(sessionKeyDTO.getKeyBase64()), "AES"),
                sessionKeyDTO.getTransformation(),
                sessionKeyDTO.getExp());

        stateStore.put(SESSION_KEY, keyHolder);
    }

    HttpHeaders createHeaders() {
        return new HttpHeaders() {{
            String username = stateStore.get("username");
            String password = stateStore.get("password");

            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(StandardCharsets.US_ASCII));
            String authHeader = "Basic " + new String(encodedAuth);
            set(HttpHeaders.AUTHORIZATION, authHeader);

            String cookie = stateStore.get("cookie");
            if (cookie != null) {
                set(HttpHeaders.AUTHORIZATION, cookie);
            }
        }};
    }

    String base64(byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }

    byte[] base64(String encoded) {
        return Base64.decodeBase64(encoded);
    }

    void checkCookie(ResponseEntity<?> response) {
        val headers = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        if (headers != null && !headers.isEmpty())
            headers.stream().findFirst()
                    .ifPresent(cookie -> stateStore.put("cookie", cookie));
    }

}
