package edu.varabei.artsiom.cyphernotebook.clientbackend.crypto;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

@Log4j2
@Service
@RequiredArgsConstructor
public class PubKeyService {

    private final String pubKeyAlgorithm;
    private final String pubKeyTransformation;

    @SneakyThrows
    public byte[] encrypt(byte[] raw, PublicKey pubKey) {
        val cipher = Cipher.getInstance(pubKeyTransformation);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(raw);
    }

    @SneakyThrows
    public byte[] decrypt(byte[] encrypted, Key privKey) {
        val cipher = Cipher.getInstance(pubKeyTransformation);
        cipher.init(Cipher.DECRYPT_MODE, privKey);
        return cipher.doFinal(encrypted);
    }

    @SneakyThrows
    public KeyPair genKeyPair() {
        val keyPairGenerator = KeyPairGenerator.getInstance(pubKeyAlgorithm);
        keyPairGenerator.initialize(4096);
        return keyPairGenerator.generateKeyPair();
    }

}
