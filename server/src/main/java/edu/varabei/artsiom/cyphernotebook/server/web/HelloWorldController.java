package edu.varabei.artsiom.cyphernotebook.server.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestController
public class HelloWorldController {

    @RequestMapping("/api/signup")
    String signup() {
        throw new RuntimeException("sign up not implemented");
    }

    @RequestMapping("/api/hello")
    String hello() {
        return "hello world";
    }

}
