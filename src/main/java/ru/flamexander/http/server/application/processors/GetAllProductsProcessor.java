package ru.flamexander.http.server.application.processors;

import com.google.gson.Gson;
import ru.flamexander.http.server.application.Item;
import ru.flamexander.http.server.application.Storage;
import ru.flamexander.http.server.application.validators.AcceptHeaderValidator;
import ru.flamexander.http.server.application.validators.RequestValidatorChain;
import ru.flamexander.http.server.server.HttpRequest;
import ru.flamexander.http.server.server.HttpResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static ru.flamexander.http.server.helpers.ContentType.APPLICATION_JSON;

public class GetAllProductsProcessor extends ValidatingRequestProcessor {

    public GetAllProductsProcessor() {
        super(new RequestValidatorChain(Arrays.asList(
                new AcceptHeaderValidator(APPLICATION_JSON)
        )));
    }

    @Override
    public HttpResponse processRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        List<Item> items = Storage.getItems();
        Gson gson = new Gson();

        httpResponse.setRequestLine("HTTP/1.1 200 OK");
        httpResponse.setHeader("Content-Type", APPLICATION_JSON.getValue());
        httpResponse.setHeader("Connection", "keep-alive");
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setBody(gson.toJson(items));

        return httpResponse;
    }
}
