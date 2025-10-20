package com.notely.api.notely.service.classes;

import org.springframework.stereotype.Service;

import com.google.common.base.Optional;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.notely.api.notely.dto.AppUserDTO;
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
    public Optional authenticate(String idToken) {
        try {

            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String firebaseUid = decodedToken.getUid();
            String email = decodedToken.getEmail();

            Optional<AppUser> existingUser = appUserRepository.findByFirebaseUid(firebaseUid);
            
            if (existingUser.isPresent()) {

                // El usuario ya existe en tu BD, lo devolvemos

                return existingUser;

            } else {

                // 3. El usuario es nuevo (o no existe en tu BD), lo registramos

                return registerNewUserFromFirebase(firebaseUid, email, decodedToken.getName());

            }

            
        } catch (FirebaseAuthException e) {
            System.err.println("Error al verificar token de Firebase: " + e.getMessage());
            return Optional.absent();

        }
    }

    @Override
    public Optional<AppUser> registerNewUserFromFirebase(String firebaseUid, String email, String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registerNewUserFromFirebase'");
    }

    @Override
    public Optional<AppUser> findAppUserByFirebaseAuthUid(String firebaseUid) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAppUserByFirebaseAuthUid'");
    }

}
