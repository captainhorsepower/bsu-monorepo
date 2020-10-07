package edu.varabei.artsiom.cyphernotebook.server.web;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@UtilityClass
public class URLUtils {

    @SneakyThrows
    public String encodeURL(String raw) {
        return URLEncoder.encode(raw, StandardCharsets.UTF_8);
    }

    public String decodeURL(String encoded) {
        return URLDecoder.decode(encoded, StandardCharsets.UTF_8);
    }

}