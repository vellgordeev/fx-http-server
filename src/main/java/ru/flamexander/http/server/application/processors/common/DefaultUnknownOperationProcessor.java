package ru.flamexander.http.server.application.processors.common;

import ru.flamexander.http.server.application.processors.AbstractRequestProcessor;
import ru.flamexander.http.server.server.HttpRequest;
import ru.flamexander.http.server.server.HttpResponse;

import java.io.IOException;

public class DefaultUnknownOperationProcessor extends AbstractRequestProcessor {

    @Override
    public HttpResponse processRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        httpResponse.setRequestLine("HTTP/1.1 200 OK");
        httpResponse.setHeader("Content-Type", "text/html");
        httpResponse.setBody("<html><body><h1>UNKNOWN OPERATION REQUEST!!!</h1></body></html>");

        return httpResponse;
    }
}
