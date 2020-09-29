package edu.varabei.artsiom.cyphernotebook.server.web;

import edu.varabei.artsiom.cyphernotebook.server.crypto.PubKeyService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.KeyGenerator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final KeyGenerator aesKeyGenerator;
    private final PubKeyService pubKeyService;

    @PostMapping("/api/signup")
    public String signup() {
        // FIXME: 9/29/20 
        throw new RuntimeException("sign up not implemented");
    }

    @PostMapping("/api/keygen")
    public ResponseEntity<?> sessionKeygen(HttpServletRequest request, @RequestBody KeygenForm form) {
        val keyHolder = new SessionKeyHolder(
                aesKeyGenerator.generateKey(),
                Duration.ofMinutes(60)
        );
        request.getSession().setAttribute(SessionKeyHolder.SESSION_KEY, keyHolder);

        val sessionKeyBytes = pubKeyService
                .encryptWithPubKey(keyHolder.getKey().getEncoded(), form.getPubKeyBase64());
        return ResponseEntity.ok(
                new SessionKeyDTO(
                        new String(Base64.encodeBase64(sessionKeyBytes)),
                        keyHolder.getExp().toEpochMilli()
                ));
    }

    @GetMapping("/api/files/{pathToFile}")
    public ResponseEntity<?> getFile(@PathVariable String pathToFile) {
        // FIXME: 9/29/20 
        return ResponseEntity.ok("file");
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
