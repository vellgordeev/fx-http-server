package ru.flamexander.http.server.application.validators;

import ru.flamexander.http.server.server.HttpRequest;
import ru.flamexander.http.server.server.HttpResponse;

import java.io.IOException;

public interface RequestValidator {
    boolean isValid(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;
}
