package edu.varabei.artsiom.cyphernotebook.server.web;

import edu.varabei.artsiom.cyphernotebook.server.crypto.AESCryptoService;
import edu.varabei.artsiom.cyphernotebook.server.crypto.PubKeyService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;

import javax.crypto.KeyGenerator;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.security.Principal;
import java.time.Duration;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final KeyGenerator aesKeyGenerator;
    private final PubKeyService pubKeyService;
    private final AESCryptoService aesService;

    @PostMapping("/api/signup")
    public String signup() {
        // FIXME: 9/29/20 
        throw new RuntimeException("sign up not implemented");
    }

    @PostMapping("/api/keygen")
    public ResponseEntity<?> sessionKeygen(HttpServletRequest request, @RequestBody KeygenForm form) {
        val keyHolder = new SessionKeyHolder(
                aesKeyGenerator.generateKey(), Duration.ofMinutes(60));
        request.getSession().setAttribute(SessionKeyHolder.SESSION_KEY, keyHolder);

        val sessionKeyBytes = pubKeyService.encryptWithPubKey(
                keyHolder.getKey().getEncoded(), form.getPubKeyBase64());
        return ResponseEntity.ok(
                new SessionKeyDTO(
                        new String(Base64.encodeBase64(sessionKeyBytes)),
                        keyHolder.getExp().toEpochMilli()
                ));
    }

    @GetMapping("/api/files/{pathToFile}")
    @SneakyThrows
    public ResponseEntity<?> getFile(@PathVariable String pathToFile, Principal principal, HttpServletRequest request) {
        val file = new FileSystemResource(principal.getName() + File.separator + pathToFile);
//        val keyHolder = (SessionKeyHolder) request.getSession().getAttribute(SessionKeyHolder.SESSION_KEY);
        //TODO 9/29/20: handle file not found and key expired not found
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .lastModified(file.lastModified())
                .body(new InputStreamResource(file.getInputStream()));
    }

    @PostMapping("/api/files/{pathToFile}")
    public ResponseEntity<?> postFile(@PathVariable String pathToFile) {
        // FIXME: 9/29/20 
        return ResponseEntity.ok("update or upload");
    }

    @DeleteMapping("/api/files/{pathToFile}")
    public ResponseEntity<?> deleteFile(@PathVariable String pathToFile) {
        // FIXME: 9/29/20
        return ResponseEntity.ok("deleted");
    }

}
