package com.notely.api.notely.service.interfaces;

import java.util.Optional;

import com.notely.api.notely.entity.AppUser;

public interface AuthServiceI {

    Optional<AppUser> authenticate(String idToken);
    AppUser registerNewUserFromFirebase(String firebaseUid, String email, String name);
    Optional<AppUser> findAppUserByFirebaseAuthUid(String firebaseUid);
}
