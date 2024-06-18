package ru.flamexander.http.server.application.processors;

import ru.flamexander.http.server.application.processors.common.DefaultUnknownOperationProcessor;
import ru.flamexander.http.server.application.validators.AcceptHeaderValidator;
import ru.flamexander.http.server.application.validators.RequestValidatorChain;
import ru.flamexander.http.server.server.HttpRequest;
import ru.flamexander.http.server.server.HttpResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static ru.flamexander.http.server.helpers.ContentType.TEXT_HTML;

public class CalculatorRequestProcessor extends ValidatingRequestProcessor {

    public CalculatorRequestProcessor() {
        super(new RequestValidatorChain(Arrays.asList(
                new AcceptHeaderValidator(TEXT_HTML)
        )));
    }

    @Override
    public HttpResponse processRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        Map<String, String> parameters = Objects.requireNonNull(httpRequest.getParameters());
        if (!(parameters.size() == 2)) {
            new DefaultUnknownOperationProcessor().processRequest(httpRequest, httpResponse);
        }
        int a = Integer.parseInt(parameters.get("a"));
        int b = Integer.parseInt(parameters.get("b"));
        int result = a + b;
        String outMessage = a + " + " + b + " = " + result;

        httpResponse.setRequestLine("HTTP/1.1 200 OK");
        httpResponse.setHeader("Content-Type", TEXT_HTML.getValue());
        httpResponse.setHeader("Connection", "keep-alive");
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setBody("<html><body><h1>" + outMessage + "</h1></body></html>");

        return httpResponse;
    }
}
