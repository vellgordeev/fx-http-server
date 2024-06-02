package ru.flamexander.http.server;

import lombok.Setter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    @Setter
    private String firstLine;
    private final Map<String, String> headers;
    @Setter
    private String body;
    private final OutputStream outputStream;
    @Setter
    private String fullResponse;


    public HttpResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.headers = new HashMap<>();
    }

    public String setHeader(String headerName, String value) {
        return headers.put(headerName, value);
    }

    public void send() throws IOException {
        if (fullResponse == null || fullResponse.isEmpty()) {
            this.fullResponse = getFullResponse();
        }

        outputStream.write(fullResponse.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
    }

    public String getFullResponse() {
        StringBuilder sb = new StringBuilder();

        sb.append(firstLine).append("\r\n");

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }

        // Empty string between headers and body
        sb.append("\r\n");

        if (body != null) {
            sb.append(body);
        }
        sb.append("\r\n\r\n");

        return sb.toString();
    }
}
