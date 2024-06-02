package ru.flamexander.http.server.application.cache;

import lombok.Getter;
import ru.flamexander.http.server.HttpRequest;
import ru.flamexander.http.server.HttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CacheManager {
    private final Map<CacheKey, CachedData> cache = new HashMap<>();

    public boolean executeIfExists(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        CacheKey cacheKey = new CacheKey(httpRequest);

        if (cache.containsKey(cacheKey)) {
            CachedData cachedData = cache.get(cacheKey);
            /*
                Here can be some logic with timestamp in cacheData, like time check
             */
            httpResponse.setFullResponse(cachedData.getResponse());
            httpResponse.send();
            return true;
        }
        return false;
    }

    public void put(HttpRequest httpRequest, HttpResponse httpResponse) {
        cache.put(new CacheKey(httpRequest), new CachedData(httpResponse.getFullResponse()));
    }

    public void clear() {
        cache.clear();
    }

    private static class CacheKey {
        @Getter
        private final Map<String, String> parameters;
        private final Map<String, String> headers;
        private final String path;

        private CacheKey(HttpRequest httpRequest) {
            this.path = httpRequest.getRouteKey();
            this.headers = new HashMap<>(httpRequest.getHeaders());
            this.parameters = new HashMap<>(httpRequest.getParameters());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CacheKey cacheKey = (CacheKey) o;
            return Objects.equals(path, cacheKey.path) &&
                    Objects.equals(headers, cacheKey.headers) &&
                    Objects.equals(parameters, cacheKey.parameters);
        }

        @Override
        public int hashCode() {
            return Objects.hash(path, headers, parameters);
        }
    }
}
