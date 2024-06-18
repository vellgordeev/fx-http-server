package ru.flamexander.http.server.application.handlers;

import ru.flamexander.http.server.server.HttpRequest;
import ru.flamexander.http.server.server.HttpResponse;

public interface Handler {
    HttpResponse handle(HttpRequest httpRequest, HttpResponse httpResponse);
}
