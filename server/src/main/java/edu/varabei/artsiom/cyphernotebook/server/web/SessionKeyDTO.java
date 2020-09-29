package edu.varabei.artsiom.cyphernotebook.server.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class SessionKeyDTO {

    @JsonProperty("AES_transformation")
    String aesTransformation;

    @JsonProperty("session_key_base64")
    String sessionKey;

    long exp;

}
