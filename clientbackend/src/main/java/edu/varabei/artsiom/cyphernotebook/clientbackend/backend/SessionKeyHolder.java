package edu.varabei.artsiom.cyphernotebook.clientbackend.backend;

import lombok.Value;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;

@Value
public class SessionKeyHolder {
    public static final String SESSION_KEY = "session-key";

    Key key;
    Instant exp;

    public SessionKeyHolder(Key key, Duration ttl) {
        this.key = key;
        this.exp = Instant.now().plus(ttl);
    }

    public boolean keyExpired() {
        return Instant.now().isAfter(exp);
    }
}
