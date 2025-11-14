package com.notely.api.notely.config;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.notely.api.notely.entity.AppUser;
import com.notely.api.notely.repository.AppUserRepository;
import com.notely.api.notely.service.interfaces.AuthServiceI;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    private final AuthServiceI authService;
    private final AppUserRepository appUserRepository;
    
    @Value("${app.dev-mode.enabled:false}")
    private boolean devModeEnabled;
    
    @Value("${app.dev-mode.mock-user.firebase-uid:dev-user-12345}")
    private String devMockFirebaseUid;
    
    @Value("${app.dev-mode.mock-user.email:dev@notely.test}")
    private String devMockEmail;
    
    @Value("${app.dev-mode.mock-user.name:Development User}")
    private String devMockName;

    public FirebaseAuthenticationFilter(AuthServiceI authService, AppUserRepository appUserRepository) {
        this.authService = authService;
        this.appUserRepository = appUserRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Development mode: Use mock user without authentication
        if (devModeEnabled) {
            AppUser devUser = getOrCreateDevUser();
            
            // Store user in security context and request attribute
            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(devUser, null, null);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Also store Firebase UID in request attribute for easy access
            request.setAttribute("firebaseUid", devUser.getFirebaseUid());
            request.setAttribute("appUser", devUser);
            
            logger.debug("Development mode: Using mock user - " + devUser.getFirebaseUid());
            filterChain.doFilter(request, response);
            return;
        }
        
        // Production mode: Use Firebase authentication
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String idToken = authHeader.substring(7);
            
            // Use AuthService to authenticate and get/create user
            Optional<AppUser> userOpt = authService.authenticate(idToken);
            
            if (userOpt.isPresent()) {
                AppUser user = userOpt.get();
                
                // Store user in security context and request attribute
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(user, null, null);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                // Also store Firebase UID in request attribute for easy access
                request.setAttribute("firebaseUid", user.getFirebaseUid());
                request.setAttribute("appUser", user);
            } else {
                logger.warn("Failed to authenticate user with Firebase token");
                // Continue without authentication - endpoints can handle this
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
    /**
     * Gets or creates the development mock user in the database
     */
    private AppUser getOrCreateDevUser() {
        Optional<AppUser> existingUser = appUserRepository.findByFirebaseUid(devMockFirebaseUid);
        
        if (existingUser.isPresent()) {
            return existingUser.get();
        }
        
        // Create dev user if it doesn't exist
        AppUser devUser = new AppUser();
        devUser.setFirebaseUid(devMockFirebaseUid);
        devUser.setEmail(devMockEmail);
        devUser.setName(devMockName);
        devUser.setCreated_at(LocalDate.now());
        devUser.setActive(true);
        
        return appUserRepository.save(devUser);
    }
}

