package com.notely.api.notely.service.classes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.notely.api.notely.dto.AppUserDTO;
import com.notely.api.notely.entity.AppUser;
import com.notely.api.notely.repository.AppUserRepository;
import com.notely.api.notely.service.interfaces.UserServiceI;
import com.notely.api.notely.util.UserContext;

@Service
public class UserService implements UserServiceI {

    private final AppUserRepository appUserRepository;

    public UserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public List<AppUserDTO> getAllActiveUsers() {
        // Get all active users - no authentication required for listing users
        // This is a public endpoint to see who's using the app
        // Privacy: Only returns basic info (name, email, uid, createdAt) - NO notes or categories
        try {
            return appUserRepository.findByIsActiveTrue()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error obtaining users: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public AppUserDTO getCurrentUser() {
        AppUser currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            throw new SecurityException("User not authenticated");
        }
        return toDTO(currentUser);
    }

    private AppUserDTO toDTO(AppUser user) {
        AppUserDTO dto = new AppUserDTO();
        dto.setUid(user.getUid());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreated_at());
        // Note: We intentionally do NOT include:
        // - notes (privacy)
        // - categories (privacy)
        // - firebaseUid (security)
        // - isActive (internal use)
        return dto;
    }
}

