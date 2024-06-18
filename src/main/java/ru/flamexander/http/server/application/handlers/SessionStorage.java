package ru.flamexander.http.server.application.handlers;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class SessionStorage {
    private final Map<String, ZonedDateTime> sessions = new HashMap<>();

    public boolean isSessionValid(String sessionId) {
        ZonedDateTime expiryDate = sessions.get(sessionId);
        if (expiryDate != null && ZonedDateTime.now().isBefore(expiryDate)) {
            return true;
        }
        sessions.remove(sessionId);
        return false;
    }

    public void createSession(String sessionId, ZonedDateTime expiryDate) {
        sessions.put(sessionId, expiryDate);
    }
}
