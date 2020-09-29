package edu.varabei.artsiom.cyphernotebook.server.web;

import lombok.RequiredArgsConstructor;
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

    @PostMapping("/api/signup")
    public String signup() {
        // FIXME: 9/29/20 
        throw new RuntimeException("sign up not implemented");
    }

    @PostMapping("/api/keygen")
    public ResponseEntity<?> genSessionKey(HttpServletRequest request) {
        final HttpSession session = request.getSession();
        final SessionKeyHolder keyHolder = new SessionKeyHolder(
                aesKeyGenerator.generateKey(),
                Duration.ofMinutes(60)
        );
        session.setAttribute(SessionKeyHolder.SESSION_KEY, keyHolder);

        //TODO 9/29/20: encrypt and send key
        return ResponseEntity.ok("encrypted key and ttl");
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
