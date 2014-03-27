/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.github.sog.session;

import com.github.sog.session.ref.ReferenceMap;
import com.github.sog.session.ref.ReferenceType;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-01-31 2:04
 * @since JDK 1.6
 */
public class SessionMonitor implements HttpSessionListener {
    protected static SessionMonitor sessionMonitor;

    protected List<HttpSessionListener>          listeners;
    protected ConcurrentMap<String, HttpSession> sessionMap;

    /**
     * Creates new session monitor.
     */
    public SessionMonitor() {
        if (sessionMonitor == null) {
            sessionMonitor = this;
            listeners = new CopyOnWriteArrayList<HttpSessionListener>();
            sessionMap = new ReferenceMap<String, HttpSession>(ReferenceType.STRONG, ReferenceType.WEAK);
        }
    }

    /**
     * Returns singleton instance of <code>SessionMonitor</code>.
     */
    public static SessionMonitor getInstance() {
        return sessionMonitor;
    }

    // ---------------------------------------------------------------- listeners

    /**
     * Registers new session listener.
     */
    public void registerListener(HttpSessionListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes existing session listener.
     */
    public void removeListener(HttpSessionListener listener) {
        listeners.remove(listener);
    }

    // ---------------------------------------------------------------- broadcast

    /**
     * Stores session in map and broadcasts event to registered listeners.
     */
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        sessionMap.putIfAbsent(session.getId(), session);

        for (HttpSessionListener listener : listeners) {
            listener.sessionCreated(httpSessionEvent);
        }
    }

    /**
     * Removes session from a map and broadcasts event to registered listeners.
     */
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        sessionMap.remove(session.getId());

        for (HttpSessionListener listener : listeners) {
            listener.sessionDestroyed(httpSessionEvent);
        }
    }

    // ---------------------------------------------------------------- map

    /**
     * Returns session for given session id. Returns <code>null</code>
     * if session expired.
     */
    public HttpSession getSession(String sessionId) {
        return sessionMap.get(sessionId);
    }

    /**
     * Returns iterator for all stored session IDs.
     * Keep in mind that session may <b>NOT</b> longer
     * exist during the iteration!
     */
    public Iterator<String> iterator() {
        return sessionMap.keySet().iterator();
    }
}
