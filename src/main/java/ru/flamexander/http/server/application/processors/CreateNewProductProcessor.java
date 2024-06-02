package ru.flamexander.http.server.application.processors;

import com.google.gson.Gson;
import ru.flamexander.http.server.HttpRequest;
import ru.flamexander.http.server.HttpResponse;
import ru.flamexander.http.server.application.Item;
import ru.flamexander.http.server.application.Storage;
import ru.flamexander.http.server.application.validators.AcceptHeaderValidator;
import ru.flamexander.http.server.application.validators.HeaderValidator;
import ru.flamexander.http.server.processors.RequestProcessor;

import java.io.IOException;

import static ru.flamexander.http.server.ContentType.APPLICATION_JSON;

public class CreateNewProductProcessor implements RequestProcessor {
    private final HeaderValidator acceptHeaderValidator;

    public CreateNewProductProcessor() {
        this.acceptHeaderValidator = new AcceptHeaderValidator(APPLICATION_JSON);
    }

    @Override
    public void execute(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        if (!acceptHeaderValidator.isValid(httpRequest, httpResponse)) {
            return;
        }

        Gson gson = new Gson();
        Item item = gson.fromJson(httpRequest.getBody(), Item.class);
        Storage.save(item);
        String jsonOutItem = gson.toJson(item);

        httpResponse.setFirstLine("HTTP/1.1 200 OK");
        httpResponse.setHeader("Content-Type", APPLICATION_JSON.getValue());
        httpResponse.setHeader("Connection", "keep-alive");
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setBody(jsonOutItem);
        httpResponse.send();
    }
}
