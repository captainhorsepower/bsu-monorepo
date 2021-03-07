package edu.varabei.artsiom.cyphernotebook.server.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class SessionKeyDTO {

    @JsonProperty("transformation")
    String transformation;

    @JsonProperty("session_key_base64")
    String keyBase64;

    @JsonProperty("exp")
    long exp;

}
