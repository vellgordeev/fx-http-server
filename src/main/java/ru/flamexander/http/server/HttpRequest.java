package ru.flamexander.http.server;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequest {
    private final String rawRequest;
    @Getter
    private String uri;
    @Getter
    private HttpMethod method;
    private Map<String, String> parameters;
    private Map<String, String> headers;
    @Getter
    private String body;

    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class.getName());

    public String getRouteKey() {
        return String.format("%s %s", method, uri);
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public Map<String, String> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    public String getHeaderValue(String headerName) {
        return headers.get(headerName);
    }

    public String setHeader(String headerName, String value) {
        return headers.put(headerName, value);
    }

    public boolean containsHeader(String key) {
        return headers.containsKey(key);
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public HttpRequest(String rawRequest) {
        this.rawRequest = rawRequest;
        this.parseRequestLine();
        this.tryToParseBody();
        this.parseHeadersLine();

        logger.debug("\n{}", rawRequest);
        logger.trace("{} {}\nParameters: {}\nBody: {}", method, uri, parameters, body); // TODO правильно все поназывать
    }

    private void tryToParseBody() {
        if (method == HttpMethod.POST || method == HttpMethod.PUT) {
            List<String> lines = rawRequest.lines().collect(Collectors.toList());
            int splitLine = -1;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).isEmpty()) {
                    splitLine = i;
                    break;
                }
            }
            if (splitLine > -1) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = splitLine + 1; i < lines.size(); i++) {
                    stringBuilder.append(lines.get(i));
                }
                this.body = stringBuilder.toString();
            }
        }
    }

    private void parseRequestLine() {
        int startIndex = rawRequest.indexOf(' ');
        int endIndex = rawRequest.indexOf(' ', startIndex + 1);
        this.uri = rawRequest.substring(startIndex + 1, endIndex);
        this.method = HttpMethod.valueOf(rawRequest.substring(0, startIndex));
        this.parameters = new HashMap<>();
        if (uri.contains("?")) {
            String[] elements = uri.split("[?]");
            this.uri = elements[0];
            String[] keysValues = elements[1].split("&");
            for (String o : keysValues) {
                String[] keyValue = o.split("=");
                this.parameters.put(keyValue[0], keyValue[1]);
            }
        }
    }

    private void parseHeadersLine() {
        List<String> lines = rawRequest.lines().collect(Collectors.toList());
        this.headers = new HashMap<>();
        int splitLine = -1;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).isEmpty()) {
                splitLine = i;
                break;
            }
        }
        if (splitLine == -1) {
            splitLine = lines.size();
        }

        for (int i = 1; i < splitLine; i++) {
            String headerLine = lines.get(i);
            int splitSymbol = headerLine.indexOf(":");
            if (splitSymbol != -1) {
                String header = headerLine.substring(0, splitSymbol).trim();
                String value = headerLine.substring(splitSymbol + 1).trim();
                this.headers.put(header, value);
            }
        }
    }
}
