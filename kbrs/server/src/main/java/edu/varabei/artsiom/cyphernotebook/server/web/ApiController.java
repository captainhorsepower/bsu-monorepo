package edu.varabei.artsiom.cyphernotebook.server.web;

import edu.varabei.artsiom.cyphernotebook.server.crypto.CryptoFilesService;
import edu.varabei.artsiom.cyphernotebook.server.crypto.PubKeyService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.KeyGenerator;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.security.Key;
import java.security.Principal;
import java.time.Duration;

import static edu.varabei.artsiom.cyphernotebook.server.web.SessionKeyHolder.SESSION_KEY;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final KeyGenerator aesKeyGenerator;
    private final PubKeyService pubKeyService;
    private final String aesTransformation;

    private final CryptoFilesService filesService;

    @PostMapping("/api/signup")
    public String signup() {
        // FIXME: 9/29/20 
        throw new RuntimeException("sign up not implemented");
    }

    @PostMapping("/api/keygen")
    public ResponseEntity<?> sessionKeygen(@RequestBody KeygenForm form,
                                           HttpServletRequest request,
                                           Principal principal) {
        val keyHolder = new SessionKeyHolder(aesKeyGenerator.generateKey(), Duration.ofMinutes(60));
        request.getSession().setAttribute(String.format("%s-%s", principal.getName(), SESSION_KEY), keyHolder);

        val sessionKeyBytes = pubKeyService.encryptWithPubKey(
                keyHolder.getKey().getEncoded(),
                form.getPubKeyAlgorithm(),
                form.getPubKeyTransformation(),
                form.getPubKeyBase64());
        return ResponseEntity.ok(
                new SessionKeyDTO(
                        aesTransformation,
                        new String(Base64.encodeBase64(sessionKeyBytes)),
                        keyHolder.getExp().toEpochMilli()
                ));
    }

    Key getSessionKey(HttpServletRequest request, Principal principal) {
        val keyHolder = (SessionKeyHolder) request.getSession()
                .getAttribute(principal.getName() + "-" + SESSION_KEY);
        if (keyHolder == null || keyHolder.keyExpired()) throw new RuntimeException("session key expired");
        return keyHolder.getKey();
    }

    String userPathToFile(Principal principal, String pathToFile) {
        pathToFile = URLUtils.decodeURL(pathToFile);
        pathToFile = FilenameUtils.separatorsToSystem(pathToFile);
        return principal.getName() + File.separator + pathToFile;
    }

    @GetMapping("/api/files")
    @SneakyThrows
    public ResponseEntity<?> getFile(@RequestParam("fileName") String pathToFile,
                                     Principal principal,
                                     HttpServletRequest request) {
        val sessionKey = getSessionKey(request, principal);
        val userPathToFile = userPathToFile(principal, pathToFile);
        InputStream encryptedFile = filesService.getFileEncrypted(userPathToFile, sessionKey);
        return ResponseEntity.ok().body(new InputStreamResource(encryptedFile));
    }

    @SneakyThrows
    @PostMapping("/api/files")
    public ResponseEntity<?> postFile(@RequestParam("fileName") String pathToFile,
                                      @RequestPart("file") MultipartFile encryptedFile,
                                      Principal principal,
                                      HttpServletRequest request) {
        val sessionKey = getSessionKey(request, principal);
        val userPathToFile = userPathToFile(principal, pathToFile);
        val bytesWritten = filesService.saveFile(userPathToFile, sessionKey, encryptedFile.getInputStream());
        return ResponseEntity.ok("saved " + bytesWritten + " bytes");
    }

    @DeleteMapping("/api/files")
    public ResponseEntity<?> deleteFile(@RequestParam("fileName") String pathToFile, Principal principal) {
        val userPathToFile = userPathToFile(principal, pathToFile);
        val deleted = filesService.deleteFile(userPathToFile);
        return ResponseEntity.ok(deleted ? "deleted" : "deleted (not found)");
    }

}
