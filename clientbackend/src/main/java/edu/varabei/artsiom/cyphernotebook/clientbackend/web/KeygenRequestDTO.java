package edu.varabei.artsiom.cyphernotebook.clientbackend.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class KeygenRequestDTO {

    @JsonProperty("pub_key_transformation")
    String pubKeyTransformation;

    @JsonProperty("pub_key_algorithm")
    String pubKeyAlgorithm;

    @JsonProperty("pub_key_base64")
    String pubKeyBase64;

}
