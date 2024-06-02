package ru.flamexander.http.server;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HttpStatus {
    OK(200, "OK"),
    NO_CONTENT(204, "No Content");

    private int code;
    private String message;
}
