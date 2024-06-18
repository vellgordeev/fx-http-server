package ru.flamexander.http.server.application.cache;

import lombok.Getter;
import ru.flamexander.http.server.helpers.HttpHeader;
import ru.flamexander.http.server.server.HttpRequest;
import ru.flamexander.http.server.server.HttpResponse;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class CacheHandler {
    private final Map<String, CacheEntry> cache = new HashMap<>();

    public boolean proceed(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        if (isNoCacheRequest(httpRequest)) {
            return false;
        }

        CacheEntry cacheEntry = cache.get(httpRequest.getRouteKey());
        if (cacheEntry != null) {
            if (cacheEntry.isExpired()) {
                cache.remove(httpRequest.getRouteKey());
                return false;
            }
            if (cacheEntry.matches(httpRequest) && isCacheValid(httpRequest, cacheEntry)) {
                sendNotModifiedResponse(httpResponse);
                return true;
            }
        }
        return false;
    }

    public void cacheResponse(HttpRequest httpRequest, HttpResponse httpResponse) {
        String cacheKey = httpRequest.getRouteKey();
        httpResponse.setHeader(HttpHeader.CACHE_CONTROL.getHeaderName(), "public, max-age=3600");
        httpResponse.setHeader(HttpHeader.ETAG.getHeaderName(), generateETag(httpResponse));
        httpResponse.setHeader(HttpHeader.LAST_MODIFIED.getHeaderName(), generateLastModified());

        CacheEntry cacheEntry = new CacheEntry(
                httpRequest.getParameters(),
                httpRequest.getBody(),
                new HashMap<>(httpResponse.getHeaders()),
                httpResponse.getBody(),
                System.currentTimeMillis() + 3600 * 1000 //expireTime in 1 hour
        );

        cache.put(cacheKey, cacheEntry);
    }

    private boolean isNoCacheRequest(HttpRequest httpRequest) {
        return httpRequest.containsHeader(HttpHeader.CACHE_CONTROL.getHeaderName()) &&
                requireNonNull(httpRequest.getHeaderValue(HttpHeader.CACHE_CONTROL.getHeaderName())).contains("no-cache");
    }

    private boolean isCacheValid(HttpRequest httpRequest, CacheEntry cacheEntry) {
        String ifNoneMatch = httpRequest.getHeaderValue(HttpHeader.IF_NONE_MATCH.getHeaderName());
        String ifModifiedSince = httpRequest.getHeaderValue(HttpHeader.IF_MODIFIED_SINCE.getHeaderName());

        return (ifNoneMatch != null && ifNoneMatch.equals(cacheEntry.getETag())) ||
                (ifModifiedSince != null && ifModifiedSince.equals(cacheEntry.getLastModified()));
    }

    private void sendNotModifiedResponse(HttpResponse httpResponse) throws IOException {
        httpResponse.setRequestLine("HTTP/1.1 304 Not Modified");
        httpResponse.send();
    }

    private String generateETag(HttpResponse httpResponse) {
        String body = httpResponse.getBody();
        int bodyHash = (body != null) ? body.hashCode() : 0;
        return "W/\"" + Integer.toHexString(bodyHash) + "\"";
    }

    private String generateLastModified() {
        return DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now());
    }


    private static class CacheEntry {
        @Getter
        private final Map<String, String> parameters;
        @Getter
        private final String requestBody;
        @Getter
        private final Map<String, String> headers;
        @Getter
        private final String responseBody;
        private final long expiryTime;

        public CacheEntry(Map<String, String> parameters, String requestBody, Map<String, String> headers, String responseBody, long expiryTime) {
            this.parameters = parameters;
            this.requestBody = requestBody;
            this.headers = headers;
            this.responseBody = responseBody;
            this.expiryTime = expiryTime;
        }

        public boolean matches(HttpRequest httpRequest) {
            return parametersMatch(httpRequest) && bodyMatches(httpRequest);
        }

        private boolean parametersMatch(HttpRequest httpRequest) {
            return this.parameters.equals(httpRequest.getParameters());
        }

        private boolean bodyMatches(HttpRequest httpRequest) {
            return (this.requestBody == null && httpRequest.getBody() == null) ||
                    (this.requestBody != null && this.requestBody.equals(httpRequest.getBody()));
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }

        public String getETag() {
            return headers.get(HttpHeader.ETAG.getHeaderName());
        }

        public String getLastModified() {
            return headers.get(HttpHeader.LAST_MODIFIED.getHeaderName());
        }
    }
}