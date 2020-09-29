package edu.varabei.artsiom.cyphernotebook.server.crypto;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.KeySpec;

@Log4j2
@Service
@RequiredArgsConstructor
public class PubKeyService {

    private final KeyFactory pubKeyFactory;
    private final String pubKeyTransformation;

    @SneakyThrows
    public byte[] encryptWithPubKey(byte[] input, KeySpec pubKeySpec) {
        final PublicKey pubKey = pubKeyFactory.generatePublic(pubKeySpec);
        final Cipher cipher = Cipher.getInstance(pubKeyTransformation);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(input);
    }

}
