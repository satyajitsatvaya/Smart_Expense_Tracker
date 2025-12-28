package com.projects.ExpenseTracker.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static String getCurrentUserEmail(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null && !authentication.isAuthenticated() ){
            return null;
        }
        return authentication.getPrincipal().toString();
    }
}
