package ru.flamexander.http.server.application.processors.common;

import ru.flamexander.http.server.application.processors.AbstractRequestProcessor;
import ru.flamexander.http.server.server.HttpRequest;
import ru.flamexander.http.server.server.HttpResponse;

import java.io.IOException;

public class DefaultOptionsProcessor extends AbstractRequestProcessor {

    @Override
    public HttpResponse processRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        httpResponse.setRequestLine("HTTP/1.1 204 No Content");
        httpResponse.setHeader("Connection", "keep-alive");
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        httpResponse.setHeader("Access-Control-Allow-Headers", "*");
        httpResponse.setHeader("Access-Control-Max-Age", "86400");

        return httpResponse;
    }
}
