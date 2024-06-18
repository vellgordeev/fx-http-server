package ru.flamexander.http.server.application.processors;

import ru.flamexander.http.server.application.validators.AcceptHeaderValidator;
import ru.flamexander.http.server.application.validators.RequestValidatorChain;
import ru.flamexander.http.server.server.HttpRequest;
import ru.flamexander.http.server.server.HttpResponse;

import java.io.IOException;
import java.util.Arrays;

import static ru.flamexander.http.server.helpers.ContentType.TEXT_HTML;

public class HelloWorldRequestProcessor extends ValidatingRequestProcessor {

    public HelloWorldRequestProcessor() {
        super(new RequestValidatorChain(Arrays.asList(
                new AcceptHeaderValidator(TEXT_HTML)
        )));
    }

    @Override
    public HttpResponse processRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        httpResponse.setRequestLine("HTTP/1.1 200 OK");
        httpResponse.setHeader("Content-Type", "text/html");
        httpResponse.setBody("<html><body><h1>Hello World!!!</h1></body></html>");

        return httpResponse;
    }
}
