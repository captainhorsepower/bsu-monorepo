package edu.varabei.artsiom.cyphernotebook.server.web;

import lombok.Value;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;

@Value
public class SessionKeyHolder {
    public static final String SESSION_KEY = "session-key";

    Key key;
    Instant exp;

    public SessionKeyHolder(Key key, Duration ttl) {
        this.key = key;
        this.exp = Instant.now().plus(ttl);
    }

}
