package ru.flamexander.http.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.flamexander.http.server.application.cache.CacheManager;
import ru.flamexander.http.server.application.processors.*;
import ru.flamexander.http.server.processors.DefaultOptionsProcessor;
import ru.flamexander.http.server.processors.DefaultStaticResourcesProcessor;
import ru.flamexander.http.server.processors.DefaultUnknownOperationProcessor;
import ru.flamexander.http.server.processors.RequestProcessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private final Map<String, RequestProcessor> router;
    private final Map<String, String> routeMethods;
    private final RequestProcessor unknownOperationRequestProcessor;
    private final RequestProcessor methodNotAllowedProcessor;
    private final RequestProcessor optionsRequestProcessor;
    private final RequestProcessor staticResourcesProcessor;
    private final CacheManager cacheManager;

    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class.getName());

    public Dispatcher() {
        this.router = new HashMap<>();
        this.routeMethods = new HashMap<>();
        this.cacheManager = new CacheManager();

        registerRoute("GET /calc", new CalculatorRequestProcessor());
        registerRoute("GET /hello", new HelloWorldRequestProcessor());
        registerRoute("GET /items", new GetAllProductsProcessor());
        registerRoute("POST /items", new CreateNewProductProcessor());

        this.unknownOperationRequestProcessor = new DefaultUnknownOperationProcessor();
        this.optionsRequestProcessor = new DefaultOptionsProcessor();
        this.staticResourcesProcessor = new DefaultStaticResourcesProcessor();
        this.methodNotAllowedProcessor = new MethodNotAllowedProcessor();

        logger.info("Диспетчер проинициализирован");
    }

    private void registerRoute(String route, RequestProcessor processor) {
        String[] parts = route.split(" ");
        String method = parts[0];
        String path = parts[1];

        this.router.put(route, processor);
        this.routeMethods.put(path, method);
    }

    public void execute(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        if (httpRequest.getMethod() == HttpMethod.OPTIONS) {
            optionsRequestProcessor.execute(httpRequest, httpResponse);
            return;
        }
        if (Files.exists(Paths.get("static/", httpRequest.getUri().substring(1)))) {
            staticResourcesProcessor.execute(httpRequest, httpResponse);
            return;
        }
        String path = httpRequest.getUri();
        String routeKey = httpRequest.getRouteKey();

        if (!router.containsKey(routeKey)) {
            if (routeMethods.containsKey(path) && !routeMethods.get(path).equals(httpRequest.getMethod().toString())) {
                methodNotAllowedProcessor.execute(httpRequest, httpResponse);
            } else {
                unknownOperationRequestProcessor.execute(httpRequest, httpResponse);
            }
            return;
        }

        if (cacheManager.executeIfExists(httpRequest, httpResponse)) {
            return;
        }

        router.get(routeKey).execute(httpRequest, httpResponse);
        cacheManager.put(httpRequest, httpResponse);
    }
}
