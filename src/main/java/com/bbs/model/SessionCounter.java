package com.bbs.model;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.ArrayList;

@Slf4j
@Component
public class SessionCounter implements HttpSessionListener {
    private List<String> sessions = new ArrayList<>();
    public static final String COUNTER = "sessionCounter";
    
    private static int count = 0 ; 
    
    public SessionCounter() {
    	log.info( "SessionCounter()");
    }

    public void sessionCreated(HttpSessionEvent event) {
        log.info("SessionCounter.sessionCreated");
        HttpSession session = event.getSession();
        sessions.add(session.getId());
        count += 1 ;
        session.getServletContext().setAttribute(SessionCounter.COUNTER, count );
    }

    public void sessionDestroyed(HttpSessionEvent event) {
        log.info("SessionCounter.sessionDestroyed");
        HttpSession session = event.getSession();
        sessions.remove(session.getId());
        count -= 1;
        count = 0 > count ? 0 : count ;
        session.getServletContext().setAttribute(SessionCounter.COUNTER, count );
    }

    public int getActiveSessionNumber() {
        return sessions.size();
    }
}