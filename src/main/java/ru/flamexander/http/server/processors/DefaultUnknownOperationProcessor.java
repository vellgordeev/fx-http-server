package ru.flamexander.http.server.processors;

import ru.flamexander.http.server.HttpRequest;
import ru.flamexander.http.server.HttpResponse;

import java.io.IOException;

public class DefaultUnknownOperationProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        httpResponse.setFirstLine("HTTP/1.1 200 OK");
        httpResponse.setHeader("Content-Type", "text/html");
        httpResponse.setBody("<html><body><h1>UNKNOWN OPERATION REQUEST!!!</h1></body></html>");
        httpResponse.send();
    }
}
