//package org.launchcode.LiftoffRecipeProject.middleware;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.launchcode.LiftoffRecipeProject.data.SessionRepository;
//import org.launchcode.LiftoffRecipeProject.models.Session;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//
//public class SessionInterceptor implements HandlerInterceptor {
//
//    @Autowired
//    private SessionRepository sessionRepository;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
//        String sessionToken = request.getHeader("X=Session-Token");
//
//        if(sessionToken != null){
//            Session session = sessionRepository.findBySessionToken(sessionToken);
//
//            if(sessionToken != null && session.getExpiration().isAfter(LocalDateTime.now())){
//                return true;
//            }
//        }
//        response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid session or session expired");
//        return false;
//
//    }
//}