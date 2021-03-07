package edu.varabei.artsiom.cyphernotebook.server.web;

import edu.varabei.artsiom.cyphernotebook.server.HMap;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.codec.binary.Base64;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiControllerTest {
    @LocalServerPort
    int randomPort;

    @BeforeEach
    public void setUp() {
        RestAssured.port = randomPort;
    }

    @Autowired
    String pubKeyTransformation;

    @Autowired
    String pubKeyAlgorithm;

    @Test
    public void getSessionKey() {
        val keyPair = keyPair();
        val publicKeyBase64 = new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()));
        System.out.println(publicKeyBase64);

        // @formatter:off
        RestAssured.given()
                .auth().preemptive().basic("artem", "password")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(HMap.so().mput("pub_key_base64", publicKeyBase64))
        .when()
                .post("/api/keygen")
        .then()
                .log().all()
                .assertThat().statusCode(200)
                .assertThat().contentType(ContentType.JSON)
                .body("session_key_base64", Matchers.notNullValue())
                .body("exp", Matchers.notNullValue())
                .cookie("JSESSIONID");
        // @formatter:on
    }

    @SneakyThrows
    public byte[] decode(byte[] encoded, Key privKey) {
        val cipher = Cipher.getInstance(pubKeyTransformation);
        cipher.init(Cipher.DECRYPT_MODE, privKey);
        return cipher.doFinal(encoded);
    }

    @SneakyThrows
    public KeyPair keyPair() {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(pubKeyAlgorithm);
        keyPairGenerator.initialize(4096);
        return keyPairGenerator.generateKeyPair();
    }

}