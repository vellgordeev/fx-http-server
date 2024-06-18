package ru.flamexander.http.server.application.validators;

import ru.flamexander.http.server.helpers.ContentType;
import ru.flamexander.http.server.server.HttpRequest;
import ru.flamexander.http.server.server.HttpResponse;

import java.io.IOException;

public class AcceptHeaderValidator implements RequestValidator {
    private final ContentType expectedContentType;

    public AcceptHeaderValidator(ContentType expectedContentType) {
        this.expectedContentType = expectedContentType;
    }

    @Override
    public boolean isValid(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String headerValue = httpRequest.getHeaderValue("Accept");
        if (headerValue == null || !headerValue.contains(expectedContentType.getValue())) {
            httpResponse.setRequestLine("HTTP/1.1 406 Not Acceptable");
            httpResponse.send();
            return false;
        }
        return true;
    }
}
