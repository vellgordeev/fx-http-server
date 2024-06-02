package ru.flamexander.http.server.application.validators;

import ru.flamexander.http.server.HttpRequest;
import ru.flamexander.http.server.HttpResponse;

import java.io.IOException;

public interface HeaderValidator {
    boolean isValid(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;
}
