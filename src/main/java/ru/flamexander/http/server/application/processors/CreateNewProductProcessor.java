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

import static ru.flamexander.http.server.helpers.ContentType.APPLICATION_JSON;

public class CreateNewProductProcessor extends ValidatingRequestProcessor {

    public CreateNewProductProcessor() {
        super(new RequestValidatorChain(Arrays.asList(
                new AcceptHeaderValidator(APPLICATION_JSON)
        )));
    }

    @Override
    public HttpResponse processRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        Gson gson = new Gson();
        Item item = gson.fromJson(httpRequest.getBody(), Item.class);
        Storage.save(item);
        String jsonOutItem = gson.toJson(item);

        httpResponse.setRequestLine("HTTP/1.1 200 OK");
        httpResponse.setHeader("Content-Type", APPLICATION_JSON.getValue());
        httpResponse.setHeader("Connection", "keep-alive");
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setBody(jsonOutItem);

        return httpResponse;
    }
}
