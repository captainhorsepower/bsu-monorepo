package edu.varabei.artsiom.cyphernotebook.server.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class KeygenForm {

    String pubKeyBase64;

    @JsonCreator
    public KeygenForm(@JsonProperty("pub_key_base64") String pubKeyBase64) {
        this.pubKeyBase64 = pubKeyBase64;
    }
}
