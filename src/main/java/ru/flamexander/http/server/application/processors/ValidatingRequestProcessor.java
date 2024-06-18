package ru.flamexander.http.server.application.processors;

import ru.flamexander.http.server.application.processors.common.DefaultUnknownOperationProcessor;
import ru.flamexander.http.server.application.validators.RequestValidatorChain;
import ru.flamexander.http.server.server.HttpRequest;
import ru.flamexander.http.server.server.HttpResponse;

import java.io.IOException;

public abstract class ValidatingRequestProcessor extends AbstractRequestProcessor {
    private final RequestValidatorChain validatorChain;

    public ValidatingRequestProcessor(RequestValidatorChain validatorChain) {
        this.validatorChain = validatorChain;
    }

    @Override
    public HttpResponse execute(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        if (!validatorChain.isValid(httpRequest, httpResponse)) {
            return new DefaultUnknownOperationProcessor().execute(httpRequest, httpResponse);
        }

        return processRequest(httpRequest, httpResponse);
    }

    public abstract HttpResponse processRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;
}
