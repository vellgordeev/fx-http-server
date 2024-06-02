package ru.flamexander.http.server.application.handlers;

import ru.flamexander.http.server.HttpRequest;
import ru.flamexander.http.server.HttpResponse;

public interface Handler {
    HttpResponse handle(HttpRequest httpRequest, HttpResponse httpResponse);
}
