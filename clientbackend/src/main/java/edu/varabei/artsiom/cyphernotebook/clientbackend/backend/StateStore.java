package edu.varabei.artsiom.cyphernotebook.clientbackend.backend;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StateStore {

    private final Map<String, Object> store = new ConcurrentHashMap<>();

    public <T> void put(String k, T val) {
        store.put(k, val);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String k) {
        return (T) store.get(k);
    }

}
