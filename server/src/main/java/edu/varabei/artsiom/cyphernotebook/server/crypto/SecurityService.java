package edu.varabei.artsiom.cyphernotebook.server.crypto;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private static final Charset charset = StandardCharsets.US_ASCII;
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    public InputStream encrypt(InputStream is, String key) {

//        return new CipherInputStream(is, )
        return null;
    }

    @SneakyThrows
    Cipher getAESCipher(Key sessionKey, AlgorithmParameterSpec iv, int mode) {
        final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(mode, sessionKey, iv);
        return cipher;
    }

}
