package ru.flamexander.http.server.application.processors;

import ru.flamexander.http.server.HttpRequest;
import ru.flamexander.http.server.HttpResponse;
import ru.flamexander.http.server.application.validators.AcceptHeaderValidator;
import ru.flamexander.http.server.application.validators.HeaderValidator;
import ru.flamexander.http.server.processors.RequestProcessor;

import java.io.IOException;

import static ru.flamexander.http.server.ContentType.TEXT_HTML;

public class CalculatorRequestProcessor implements RequestProcessor {
    private final HeaderValidator acceptHeaderValidator;

    public CalculatorRequestProcessor() {
        this.acceptHeaderValidator = new AcceptHeaderValidator(TEXT_HTML);
    }

    @Override
    public void execute(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        if (!acceptHeaderValidator.isValid(httpRequest, httpResponse)) {
            return;
        }

        int a = Integer.parseInt(httpRequest.getParameter("a"));
        int b = Integer.parseInt(httpRequest.getParameter("b"));
        int result = a + b;
        String outMessage = a + " + " + b + " = " + result;

        httpResponse.setFirstLine("HTTP/1.1 200 OK");
        httpResponse.setHeader("Content-Type", TEXT_HTML.getValue());
        httpResponse.setHeader("Connection", "keep-alive");
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setBody("<html><body><h1>" + outMessage + "</h1></body></html>");
        httpResponse.send();
    }
}
