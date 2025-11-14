package com.notely.api.notely.service.classes;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.notely.api.notely.entity.AppUser;
import com.notely.api.notely.repository.AppUserRepository;
import com.notely.api.notely.service.interfaces.AuthServiceI;

@Service
public class AuthService implements AuthServiceI{

    private AppUserRepository appUserRepository;

    public AuthService(AppUserRepository appUserRepository){
        this.appUserRepository = appUserRepository;
    }

    @Override
    public Optional<AppUser> authenticate(String idToken) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String firebaseUid = decodedToken.getUid();
            String email = decodedToken.getEmail();
            String name = decodedToken.getName();

            Optional<AppUser> existingUser = appUserRepository.findByFirebaseUid(firebaseUid);
            
            if (existingUser.isPresent()) {
                return existingUser;
            } else {
                // User doesn't exist, register them
                return Optional.of(registerNewUserFromFirebase(firebaseUid, email, name));
            }
            
        } catch (FirebaseAuthException e) {
            System.err.println("Error verifying Firebase token: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public AppUser registerNewUserFromFirebase(String firebaseUid, String email, String name) {
        AppUser newUser = new AppUser();
        newUser.setFirebaseUid(firebaseUid);
        newUser.setEmail(email != null ? email : "");
        newUser.setName(name != null ? name : "");
        newUser.setCreated_at(LocalDate.now());
        newUser.setActive(true);
        
        return appUserRepository.save(newUser);
    }

    @Override
    public Optional<AppUser> findAppUserByFirebaseAuthUid(String firebaseUid) {
        return appUserRepository.findByFirebaseUid(firebaseUid);
    }

}
