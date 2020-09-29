package edu.varabei.artsiom.cyphernotebook.clientbackend.crypto;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

@Log4j2
@Service
@RequiredArgsConstructor
public class PubKeyService {

    private final KeyFactory pubKeyFactory;
    private final String pubKeyTransformation;

    public byte[] encryptWithPubKey(byte[] input, String base64PubKey) {
        val keySpec = new X509EncodedKeySpec(Base64.decodeBase64(base64PubKey));
        return encryptWithPubKey(input, keySpec);
    }

    @SneakyThrows
    public byte[] encryptWithPubKey(byte[] input, KeySpec pubKeySpec) {
        final PublicKey pubKey = pubKeyFactory.generatePublic(pubKeySpec);
        final Cipher cipher = Cipher.getInstance(pubKeyTransformation);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(input);
    }

}
