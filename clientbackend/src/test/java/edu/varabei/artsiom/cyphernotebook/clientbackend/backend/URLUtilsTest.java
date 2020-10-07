package edu.varabei.artsiom.cyphernotebook.clientbackend.backend;

import lombok.SneakyThrows;
import lombok.val;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class URLUtilsTest {

    @Test
    public void encode() {
        val unixPath = "path/to/file.txt";
        val dumbAssPath = "path\\to\\file.txt";
        Assertions.assertEquals(
                unixPath,
                URLUtils.decodeURL(
                        URLUtils.encodeURL(unixPath))
        );
        Assertions.assertEquals(
                dumbAssPath,
                URLUtils.decodeURL(
                        URLUtils.encodeURL(dumbAssPath))
        );
        System.out.println(URLUtils.encodeURL(unixPath));
        System.out.println(URLUtils.encodeURL(dumbAssPath));
    }

    @Test
    @SneakyThrows
    public void buildingURI() {
        String pathToFile = "meem.jpg";
        System.out.println(new URIBuilder("http://localhost:8080/api/files")
                .addParameter("file", pathToFile)
                .build());
    }

}