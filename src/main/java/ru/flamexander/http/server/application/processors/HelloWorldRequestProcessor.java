package ru.flamexander.http.server.application.processors;

import ru.flamexander.http.server.HttpRequest;
import ru.flamexander.http.server.HttpResponse;
import ru.flamexander.http.server.application.validators.AcceptHeaderValidator;
import ru.flamexander.http.server.application.validators.HeaderValidator;
import ru.flamexander.http.server.processors.RequestProcessor;

import java.io.IOException;

import static ru.flamexander.http.server.ContentType.TEXT_HTML;

public class HelloWorldRequestProcessor implements RequestProcessor {
    private final HeaderValidator acceptHeaderValidator;

    public HelloWorldRequestProcessor() {
        this.acceptHeaderValidator = new AcceptHeaderValidator(TEXT_HTML);
    }

    @Override
    public void execute(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        if (!acceptHeaderValidator.isValid(httpRequest, httpResponse)) {
            return;
        }

        httpResponse.setFirstLine("HTTP/1.1 200 OK");
        httpResponse.setHeader("Content-Type", "text/html");
        httpResponse.setBody("<html><body><h1>Hello World!!!</h1></body></html>");
        httpResponse.send();
    }
}
