package ru.flamexander.http.server.application.processors.common;

import ru.flamexander.http.server.application.processors.AbstractRequestProcessor;
import ru.flamexander.http.server.server.HttpRequest;
import ru.flamexander.http.server.server.HttpResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DefaultStaticResourcesProcessor extends AbstractRequestProcessor {

    @Override
    public HttpResponse processRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String filename = httpRequest.getUri().substring(1);
        Path filePath = Paths.get("static/", filename);
        String fileType = filename.substring(filename.lastIndexOf(".") + 1);
        byte[] fileData = Files.readAllBytes(filePath);

        if (fileType.equals("pdf")) {
            httpResponse.setHeader("Content-Disposition", "attachment;filename=" + filename);
        }

        httpResponse.setRequestLine("HTTP/1.1 200 OK");
        httpResponse.setHeader("Content-Length", String.valueOf(fileData.length));

        return httpResponse;
    }
}
