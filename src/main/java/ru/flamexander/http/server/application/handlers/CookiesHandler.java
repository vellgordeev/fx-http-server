package ru.flamexander.http.server.application.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.flamexander.http.server.helpers.HttpHeader;
import ru.flamexander.http.server.server.HttpRequest;
import ru.flamexander.http.server.server.HttpResponse;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class CookiesHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(CookiesHandler.class.getName());
    private final SessionStorage sessionStorage = new SessionStorage();

    @Override
    public HttpResponse handle(HttpRequest httpRequest, HttpResponse httpResponse) {
        Map<String, String> cookies = parseCookies(httpRequest.getHeaderValue(HttpHeader.COOKIE.getHeaderName()));

        if (cookies.containsKey("sessionID")) {
            String sessionId = cookies.get("sessionID");
            logger.trace("User session ID: {}", sessionId);

            if (!sessionStorage.isSessionValid(sessionId)) {
                setNewSessionCookie(httpResponse);
            } else {
                // logic to work with user session
            }
        } else {
            setNewSessionCookie(httpResponse);
        }

        return httpResponse;
    }

    private void setNewSessionCookie(HttpResponse httpResponse) {
        String sessionId = UUID.randomUUID().toString();
        ZonedDateTime expiryDate = ZonedDateTime.now().plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);

        sessionStorage.createSession(sessionId, expiryDate);
        httpResponse.setHeader(HttpHeader.SET_COOKIE.getHeaderName(),
                String.format("sessionID=%s; Expires=%s; Path=/; HttpOnly", sessionId, formatter.format(expiryDate)));
    }

    private Map<String, String> parseCookies(String cookieHeader) {
        Map<String, String> cookies = new HashMap<>();
        if (cookieHeader != null) {
            String[] cookiePairs = cookieHeader.split("; ");
            for (String cookiePair : cookiePairs) {
                String[] keyValue = cookiePair.split("=", 2);
                if (keyValue.length == 2) {
                    cookies.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return cookies;
    }
}
