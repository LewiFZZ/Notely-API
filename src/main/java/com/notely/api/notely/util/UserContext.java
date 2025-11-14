package com.notely.api.notely.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.notely.api.notely.entity.AppUser;

/**
 * Utility class to get the current authenticated user from the request context.
 * This extracts the user that was set by FirebaseAuthenticationFilter.
 */
public class UserContext {
    
    /**
     * Gets the current authenticated user from the security context.
     * @return The AppUser if authenticated, null otherwise
     */
    public static AppUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AppUser) {
            return (AppUser) authentication.getPrincipal();
        }
        return null;
    }
    
    /**
     * Gets the Firebase UID of the current authenticated user.
     * @return The Firebase UID if authenticated, null otherwise
     */
    public static String getCurrentFirebaseUid() {
        AppUser user = getCurrentUser();
        return user != null ? user.getFirebaseUid() : null;
    }
    
    /**
     * Gets the Firebase UID from request attribute (alternative method).
     * @return The Firebase UID if present in request, null otherwise
     */
    public static String getFirebaseUidFromRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            return (String) requestAttributes.getAttribute("firebaseUid", RequestAttributes.SCOPE_REQUEST);
        }
        return null;
    }
    
    /**
     * Gets the AppUser from request attribute (alternative method).
     * @return The AppUser if present in request, null otherwise
     */
    public static AppUser getAppUserFromRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            return (AppUser) requestAttributes.getAttribute("appUser", RequestAttributes.SCOPE_REQUEST);
        }
        return null;
    }
}

