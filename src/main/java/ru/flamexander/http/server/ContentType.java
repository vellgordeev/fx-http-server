package ru.flamexander.http.server;

import lombok.Getter;

@Getter
public enum ContentType {
    APPLICATION_JSON("application/json"),
    TEXT_HTML("text/html"),
    TEXT_PLAIN("text/plain"),
    APPLICATION_XML("application/xml"),
    TEXT_CSS("text/css"),
    APPLICATION_JAVASCRIPT("application/javascript");

    private final String value;

    ContentType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
