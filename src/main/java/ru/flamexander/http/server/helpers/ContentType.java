package ru.flamexander.http.server.helpers;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ContentType {
    APPLICATION_JSON("application/json"),
    TEXT_HTML("text/html"),
    TEXT_PLAIN("text/plain"),
    APPLICATION_XML("application/xml"),
    TEXT_CSS("text/css"),
    APPLICATION_JAVASCRIPT("application/javascript");

    private final String value;

    @Override
    public String toString() {
        return value;
    }
}
