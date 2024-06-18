package ru.flamexander.http.server.application.processors;

import ru.flamexander.http.server.server.HttpRequest;
import ru.flamexander.http.server.server.HttpResponse;

import java.io.IOException;

public abstract class AbstractRequestProcessor implements RequestProcessor {

    @Override
    public HttpResponse execute(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        return processRequest(httpRequest, httpResponse);
    }

    public abstract HttpResponse processRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;
}
