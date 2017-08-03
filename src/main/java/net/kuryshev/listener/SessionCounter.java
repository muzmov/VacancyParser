package net.kuryshev.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionCounter implements HttpSessionListener {
    private static int sessions;

    public static int getSessions() {
        return sessions;
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        sessions++;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        sessions--;
    }
}
