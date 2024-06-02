package ru.flamexander.http.server.application.processors;

import ru.flamexander.http.server.HttpRequest;
import ru.flamexander.http.server.HttpResponse;
import ru.flamexander.http.server.processors.RequestProcessor;

import java.io.IOException;

public class MethodNotAllowedProcessor implements RequestProcessor {

    @Override
    public void execute(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        httpResponse.setFirstLine("HTTP/1.1 405 Method Not Allowed");
        httpResponse.setHeader("Content-Length", "0");
        httpResponse.setHeader("Connection", "close");
        httpResponse.send();
    }
}
