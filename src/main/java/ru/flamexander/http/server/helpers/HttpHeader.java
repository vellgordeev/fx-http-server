package ru.flamexander.http.server.helpers;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HttpHeader {
    CACHE_CONTROL("Cache-Control"),
    ETAG("ETag"),
    LAST_MODIFIED("Last-Modified"),
    IF_NONE_MATCH("If-None-Match"),
    IF_MODIFIED_SINCE("If-Modified-Since"),
    COOKIE("Cookie"),
    SET_COOKIE("Set-Cookie");

    private final String headerName;
}
