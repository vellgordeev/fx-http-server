package ru.flamexander.http.server.application.processors.common;

import ru.flamexander.http.server.application.processors.AbstractRequestProcessor;
import ru.flamexander.http.server.server.HttpRequest;
import ru.flamexander.http.server.server.HttpResponse;

import java.io.IOException;

public class MethodNotAllowedProcessor extends AbstractRequestProcessor {

    @Override
    public HttpResponse processRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        httpResponse.setRequestLine("HTTP/1.1 405 Method Not Allowed");
        httpResponse.setHeader("Content-Length", "0");
        httpResponse.setHeader("Connection", "close");

        return httpResponse;
    }
}
