package ru.flamexander.http.server.processors;

import ru.flamexander.http.server.HttpRequest;
import ru.flamexander.http.server.HttpResponse;

import java.io.IOException;

public class DefaultOptionsProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        httpResponse.setFirstLine("HTTP/1.1 204 No Content");
        httpResponse.setHeader("Connection", "keep-alive");
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        httpResponse.setHeader("Access-Control-Allow-Headers", "*");
        httpResponse.setHeader("Access-Control-Max-Age", "86400");

        httpResponse.send();
    }
}
