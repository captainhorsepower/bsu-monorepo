package edu.varabei.artsiom.cyphernotebook.server.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.KeyGenerator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
public class HelloWorldController {

    private final KeyGenerator aesKeyGenerator;

    @RequestMapping("/api/signup")
    public String signup() {
        throw new RuntimeException("sign up not implemented");
    }

    @RequestMapping("/api/keygen")
    public ResponseEntity<?> genSessionKey(HttpServletRequest request) {
        final HttpSession session = request.getSession();
        final SessionKeyHolder keyHolder = new SessionKeyHolder(
                aesKeyGenerator.generateKey(),
                Duration.ofMinutes(60)
        );
        session.setAttribute(SessionKeyHolder.SESSION_KEY, keyHolder);

        return ResponseEntity.ok("encrypted key and ttl");
    }

}
