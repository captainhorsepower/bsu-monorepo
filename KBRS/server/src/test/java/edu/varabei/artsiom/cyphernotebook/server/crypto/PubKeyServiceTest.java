package edu.varabei.artsiom.cyphernotebook.server.crypto;

import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.X509EncodedKeySpec;

@SpringBootTest
class PubKeyServiceTest {

    @Value("RSA")
    String pubKeyAlgorithm;

    @Value("RSA/ECB/PKCS1Padding")
    String pubKeyTransformation;

    @Autowired
    PubKeyService pubKeyService;

    @Test
    public void test() {
        val secretMessage = "secret message";

        val keyPair = keyPair();
        val pubKeySpec = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());

        val encryptedBytes = pubKeyService.encryptWithPubKey(secretMessage.getBytes(), pubKeyAlgorithm, pubKeyTransformation, pubKeySpec);
        val decryptedBytes = decode(encryptedBytes, keyPair.getPrivate());

        Assertions.assertEquals(secretMessage, new String(decryptedBytes));
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