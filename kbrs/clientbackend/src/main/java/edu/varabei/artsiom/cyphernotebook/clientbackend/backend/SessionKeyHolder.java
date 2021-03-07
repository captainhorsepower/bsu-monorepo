package edu.varabei.artsiom.cyphernotebook.clientbackend.backend;

import lombok.Value;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;

@Value
public class SessionKeyHolder {
    public static final String SESSION_KEY = "session-key";

    Key key;
    String transformation;
    Instant exp;

    public SessionKeyHolder(Key key, String transformation, long exp) {
        this.key = key;
        this.transformation = transformation;
        this.exp = Instant.ofEpochMilli(exp);
    }

    public boolean keyExpired() {
        return Instant.now().isAfter(exp);
    }
}
