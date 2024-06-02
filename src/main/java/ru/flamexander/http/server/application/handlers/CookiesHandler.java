package ru.flamexander.http.server.application.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.flamexander.http.server.HttpRequest;
import ru.flamexander.http.server.HttpResponse;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

public class CookiesHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(CookiesHandler.class.getName());

    @Override
    public HttpResponse handle(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (httpRequest.containsHeader("Cookie")) {
            logger.trace("User session ID: {}", httpRequest.getHeaderValue("Cookie"));
            /*
                There will be some logic to work with user session
             */
        } else {
            String sessionId = UUID.randomUUID().toString();
            ZonedDateTime expiryDate = ZonedDateTime.now().plusDays(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);

            httpResponse.setHeader("Set-Cookie", "sessionID=" + sessionId + "; Expires=" + formatter.format(expiryDate));
        }

        return httpResponse;
    }
}
