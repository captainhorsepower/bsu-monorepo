package edu.varabei.artsiom.cyphernotebook.server.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class KeygenForm {

    String pubKeyTransformation;
    String pubKeyAlgorithm;
    String pubKeyBase64;

    public KeygenForm(@JsonProperty("pub_key_transformation") String pubKeyTransformation,
                      @JsonProperty("pub_key_algorithm") String pubKeyAlgorithm,
                      @JsonProperty("pub_key_base64") String pubKeyBase64) {
        this.pubKeyTransformation = pubKeyTransformation;
        this.pubKeyAlgorithm = pubKeyAlgorithm;
        this.pubKeyBase64 = pubKeyBase64;
    }
}
