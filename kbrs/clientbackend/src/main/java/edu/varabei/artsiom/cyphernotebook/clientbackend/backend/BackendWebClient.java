package edu.varabei.artsiom.cyphernotebook.clientbackend.backend;

import edu.varabei.artsiom.cyphernotebook.clientbackend.StateStore;
import edu.varabei.artsiom.cyphernotebook.clientbackend.crypto.AESCryptoService;
import edu.varabei.artsiom.cyphernotebook.clientbackend.crypto.PubKeyService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BackendWebClient {

    public static final String KEY_PAIR = "key-pair";
    public static final String SESSION_KEY = "session-key";

    //TODO 9/30/20: try webClient?
    private final RestTemplate restTemplate;
    private final StateStore stateStore;
    private final PubKeyService pubKeyService;
    private final AESCryptoService aesCryptoService;

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

    public SessionKeyHolder getSessionKey() {
        final KeyPair keyPair = stateStore.get(KEY_PAIR);
        val request = new KeygenRequestDTO(
                pubKeyTransformation,
                pubKeyAlgorithm,
                base64(keyPair.getPublic().getEncoded()));

        val response = restTemplate.exchange("http://localhost:8080/api/keygen",
                HttpMethod.POST, new HttpEntity<>(request, createHeaders()), SessionKeyDTO.class);

        checkCookie(response);

        val sessionKeyDTO = response.getBody();
        val aesKeyBytes = pubKeyService.decrypt(base64(sessionKeyDTO.getKeyBase64()), keyPair.getPrivate());
        val keyHolder = new SessionKeyHolder(
                new SecretKeySpec(aesKeyBytes, "AES"),
                sessionKeyDTO.getTransformation(),
                sessionKeyDTO.getExp());

        stateStore.put(SESSION_KEY, keyHolder);
        return keyHolder;
    }

    SessionKeyHolder keyHolder() {
        //TODO 9/30/20: check exp
        return stateStore.get(SESSION_KEY);
    }

    // much thanks to https://medium.com/red6-es/uploading-a-file-with-a-filename-with-spring-resttemplate-8ec5e7dc52ca
    @SneakyThrows
    public String uploadFile(InputStream rawContent, String pathToFile) {
        pathToFile = URLUtils.encodeURL(pathToFile);
        val keyHolder = keyHolder();
        val encryptedContent = aesCryptoService.encrypt(rawContent, keyHolder.getTransformation(), keyHolder.getKey());

        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // This nested HttpEntiy is important to create the correct
        // Content-Disposition entry with metadata "name" and "filename"
        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
        ContentDisposition contentDisposition = ContentDisposition
                .builder("form-data")
                .name("file")
                .filename("localfile")
                .build();
        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        HttpEntity<byte[]> fileEntity = new HttpEntity<>(encryptedContent.readAllBytes(), fileMap);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileEntity);

        val response = restTemplate.exchange(
                new URIBuilder("http://localhost:8080/api/files")
                        .addParameter("fileName", pathToFile)
                        .build(),
                HttpMethod.POST, new HttpEntity<>(body, headers), String.class);

        checkCookie(response);
        return response.getBody();
    }

    @SneakyThrows
    public Number getFile(OutputStream output, String pathToFile) {
        pathToFile = URLUtils.encodeURL(pathToFile);
        val keyHolder = keyHolder();

        return restTemplate.execute(
                new URIBuilder("http://localhost:8080/api/files")
                        .addParameter("fileName", pathToFile)
                        .build(),
                HttpMethod.GET,
                httpRequest -> httpRequest.getHeaders().addAll(createHeaders()),
                (ResponseExtractor<Number>) httpResponse ->
                        aesCryptoService.decrypt(
                                httpResponse.getBody(),
                                keyHolder.getTransformation(),
                                keyHolder.getKey())
                                .transferTo(output));
    }

    public String deleteFile(String pathToFile) {
        pathToFile = URLUtils.encodeURL(pathToFile);
        val response = restTemplate.exchange(
                "http://localhost:8080/api/files" + pathToFile,
                HttpMethod.DELETE, new HttpEntity<>(createHeaders()), String.class);
        return response.getBody();
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
                set(HttpHeaders.COOKIE, cookie);
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
