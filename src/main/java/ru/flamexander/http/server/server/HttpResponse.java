package ru.flamexander.http.server.server;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    @Setter
    @Getter
    private String requestLine;
    @Getter
    private final Map<String, String> headers;
    @Setter
    @Getter
    private String body;
    private final OutputStream outputStream;
    @Setter
    private String fullResponse;
    private static final String CRLF = "\r\n";
    private static final int MAX_RESPONSE_SIZE = 8192;

    public HttpResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.headers = new HashMap<>();
    }

    public String setHeader(String headerName, String value) {
        return headers.put(headerName, value);
    }

    public String getHeaderValue(String headerName) {
        return headers.get(headerName);
    }

    public void send() throws IOException {
        if (fullResponse == null || fullResponse.isEmpty()) {
            this.fullResponse = getFullResponse();
        }

        outputStream.write(fullResponse.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
    }

    public String getFullResponse() {
        StringBuilder sb = new StringBuilder(MAX_RESPONSE_SIZE);
        sb.append(requestLine).append(CRLF);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(CRLF);
        }

        // Empty string between headers and body
        sb.append(CRLF);

        if (body != null) {
            sb.append(body);
        }
        sb.append(CRLF + CRLF);

        return sb.toString();
    }
}
