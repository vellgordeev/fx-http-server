package ru.flamexander.http.server.processors;

import ru.flamexander.http.server.HttpRequest;
import ru.flamexander.http.server.HttpResponse;

import java.io.IOException;

public interface RequestProcessor {
    void execute(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;
}
