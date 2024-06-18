package ru.flamexander.http.server.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.flamexander.http.server.application.cache.CacheHandler;
import ru.flamexander.http.server.application.processors.*;
import ru.flamexander.http.server.application.processors.common.DefaultOptionsProcessor;
import ru.flamexander.http.server.application.processors.common.DefaultStaticResourcesProcessor;
import ru.flamexander.http.server.application.processors.common.DefaultUnknownOperationProcessor;
import ru.flamexander.http.server.application.processors.common.MethodNotAllowedProcessor;
import ru.flamexander.http.server.helpers.HttpMethod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Dispatcher {
    private final Map<String, RequestProcessor> router;
    private final Map<String, HttpMethod> routeMethods;
    private final RequestProcessor unknownOperationRequestProcessor;
    private final RequestProcessor methodNotAllowedProcessor;
    private final RequestProcessor optionsRequestProcessor;
    private final RequestProcessor staticResourcesProcessor;
    private final Set<String> cacheableRoutes;
    private final CacheHandler cacheHandler;

    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class.getName());

    public Dispatcher() {
        this.router = new HashMap<>();
        this.routeMethods = new HashMap<>();
        this.cacheableRoutes = new HashSet<>();
        this.cacheHandler = new CacheHandler();

        registerRoute("GET /calc", new CalculatorRequestProcessor(), true);
        registerRoute("GET /hello", new HelloWorldRequestProcessor(), false);
        registerRoute("GET /items", new GetAllProductsProcessor(), true);
        registerRoute("POST /items", new CreateNewProductProcessor(), false);

        this.unknownOperationRequestProcessor = new DefaultUnknownOperationProcessor();
        this.optionsRequestProcessor = new DefaultOptionsProcessor();
        this.staticResourcesProcessor = new DefaultStaticResourcesProcessor();
        this.methodNotAllowedProcessor = new MethodNotAllowedProcessor();

        logger.info("Диспетчер проинициализирован");
    }

    private void registerRoute(String route, RequestProcessor processor, boolean cacheable) {
        String[] parts = Objects.requireNonNull(route).split(" ");
        HttpMethod method = HttpMethod.valueOf(parts[0]);
        String path = parts[1];

        this.router.put(route, processor);
        this.routeMethods.put(path, method);

        if (cacheable) {
            cacheableRoutes.add(route);
        }
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
            if (routeMethods.containsKey(path) && !(routeMethods.get(path) == httpRequest.getMethod())) {
                methodNotAllowedProcessor.execute(httpRequest, httpResponse);
            } else {
                unknownOperationRequestProcessor.execute(httpRequest, httpResponse);
            }
            return;
        }

        if (cacheableRoutes.contains(httpRequest.getRouteKey())) {
            if (!cacheHandler.proceed(httpRequest, httpResponse)) {
                HttpResponse processedResponse = router.get(routeKey).execute(httpRequest, httpResponse);
                cacheHandler.cacheResponse(httpRequest, processedResponse);
                processedResponse.send();
                return;
            }
        }
        router.get(routeKey).execute(httpRequest, httpResponse).send();
    }
}
