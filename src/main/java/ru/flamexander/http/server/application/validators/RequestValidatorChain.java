package ru.flamexander.http.server.application.validators;

import ru.flamexander.http.server.server.HttpRequest;
import ru.flamexander.http.server.server.HttpResponse;

import java.io.IOException;
import java.util.List;

public class RequestValidatorChain {

    private final List<RequestValidator> validators;

    public RequestValidatorChain(List<RequestValidator> validators) {
        this.validators = validators;
    }

    public boolean isValid(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        for (RequestValidator validator : validators) {
            if (!validator.isValid(httpRequest, httpResponse)) {
                return false;
            }
        }
        return true;
    }
}
