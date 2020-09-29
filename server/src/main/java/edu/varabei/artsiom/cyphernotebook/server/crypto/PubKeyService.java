package edu.varabei.artsiom.cyphernotebook.server.crypto;

import lombok.SneakyThrows;
import lombok.val;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

@Service
public class PubKeyService {

    public byte[] encryptWithPubKey(byte[] input, String alg, String transformation, String base64PubKey) {
        val keySpec = new X509EncodedKeySpec(Base64.decodeBase64(base64PubKey));
        return encryptWithPubKey(input, alg, transformation, keySpec);
    }

    @SneakyThrows
    public byte[] encryptWithPubKey(byte[] input, String alg, String transformation, KeySpec pubKeySpec) {
        val pubKeyFactory = KeyFactory.getInstance(alg);
        val pubKey = pubKeyFactory.generatePublic(pubKeySpec);
        val cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(input);
    }

}
