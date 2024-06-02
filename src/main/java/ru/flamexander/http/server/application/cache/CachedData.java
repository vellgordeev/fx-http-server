package ru.flamexander.http.server.application.cache;

import lombok.Getter;

@Getter
public class CachedData {
    private final String response;
    private final long timestamp;

    public CachedData(String response) {
        this.timestamp = System.currentTimeMillis();
        this.response = response;
    }

}
