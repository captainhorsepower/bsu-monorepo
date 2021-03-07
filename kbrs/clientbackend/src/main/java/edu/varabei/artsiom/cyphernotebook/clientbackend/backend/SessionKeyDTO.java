package edu.varabei.artsiom.cyphernotebook.clientbackend.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class SessionKeyDTO {

    String transformation;
    String keyBase64;
    long exp;

    public SessionKeyDTO(@JsonProperty("transformation")String transformation,
                         @JsonProperty("session_key_base64")String keyBase64,
                         @JsonProperty("exp") long exp) {
        this.transformation = transformation;
        this.keyBase64 = keyBase64;
        this.exp = exp;
    }

}
