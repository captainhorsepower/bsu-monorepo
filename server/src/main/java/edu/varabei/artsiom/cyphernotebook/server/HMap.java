package edu.varabei.artsiom.cyphernotebook.server;

import java.util.LinkedHashMap;

public class HMap<K, V> extends LinkedHashMap<K, V> {

    public static HMap<String, Object> so() {
        return new HMap<>();
    }

    public HMap<K, V> mput(K key, V val) {
        put(key, val);
        return this;
    }
}
