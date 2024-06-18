package ru.flamexander.http.server.application.processors;

import ru.flamexander.http.server.server.HttpRequest;
import ru.flamexander.http.server.server.HttpResponse;

import java.io.IOException;

public interface RequestProcessor {
    HttpResponse execute(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;
}
