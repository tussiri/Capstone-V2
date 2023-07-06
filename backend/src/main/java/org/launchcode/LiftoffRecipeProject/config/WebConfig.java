//package org.launchcode.LiftoffRecipeProject.config;
//
//import org.launchcode.LiftoffRecipeProject.middleware.SessionInterceptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//public class WebConfig implements WebMvcConfigurer {
//
//    @Autowired
//    private SessionInterceptor sessionInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry){
//        registry.addInterceptor(sessionInterceptor).addPathPatterns("/api/**");
//    }
//}