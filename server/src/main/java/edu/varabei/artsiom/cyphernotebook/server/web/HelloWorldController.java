package edu.varabei.artsiom.cyphernotebook.server.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @RequestMapping("/api/signup")
    public String signup() {
        throw new RuntimeException("sign up not implemented");
    }

    @RequestMapping("/api/keygen")
    public ResponseEntity<?> genSessionKey() {
        return ResponseEntity.ok("encrypted key");
    }

}
