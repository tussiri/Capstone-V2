//package org.launchcode.LiftoffRecipeProject.models;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//
//import java.time.LocalDateTime;
//
//@Entity
//public class Session extends AbstractEntity{
//
//    @Column(nullable=false)
//    private String sessionToken;
//
//    @ManyToOne
//    @JoinColumn(nullable=false)
//    private User user;
//
//    @Column(nullable=false)
//    private LocalDateTime expiration;
//
//    public Session(String sessionToken, User user, LocalDateTime expiration) {
//        this.sessionToken = sessionToken;
//        this.user = user;
//        this.expiration = expiration;
//    }
//
//    public String getSessionToken() {
//        return sessionToken;
//    }
//
//    public void setSessionToken(String sessionToken) {
//        this.sessionToken = sessionToken;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//
//    public LocalDateTime getExpiration() {
//        return expiration;
//    }
//
//    public void setExpiration(LocalDateTime expiration) {
//        this.expiration = expiration;
//    }
//
//    public boolean isExpired(){
//        return LocalDateTime.now().isAfter(expiration);
//    }
//}