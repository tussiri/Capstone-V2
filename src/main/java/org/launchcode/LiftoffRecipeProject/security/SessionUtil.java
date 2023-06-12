//package org.launchcode.LiftoffRecipeProject.security;
//
//import org.launchcode.LiftoffRecipeProject.models.Session;
//import org.launchcode.LiftoffRecipeProject.models.User;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//@Component
//public class SessionUtil {
//
//    private final Map<String, Session> sessionStore = new HashMap<>();
//
//    public String createSession(User user){
//        String sessionToken=generateSessionToken();
//        LocalDateTime expiration = LocalDateTime.now().plusHours(2);
//        Session session = new Session(sessionToken, user, expiration);
//        sessionStore.put(sessionToken, session);
//        return sessionToken;
//    }
//
//    public boolean isValidSession(String sessionToken){
//        Session session = sessionStore.get(sessionToken);
//        return session!=null && !session.isExpired();
//    }
//
//    public User getUserFromSession(String sessionToken){
//        Session session = sessionStore.get(sessionToken);
//        return session !=null ? session.getUser():null;
//    }
//
//    private String generateSessionToken(){
//        return UUID.randomUUID().toString();
//    }
//
//}