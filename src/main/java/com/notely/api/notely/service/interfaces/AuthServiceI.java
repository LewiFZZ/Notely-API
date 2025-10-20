package com.notely.api.notely.service.interfaces;


import com.google.common.base.Optional;
import com.notely.api.notely.entity.AppUser;

public interface AuthServiceI {

    public Optional<AppUser> authenticate(String idToken);
    public Optional<AppUser> registerNewUserFromFirebase(String firebaseUid, String email, String name);
    public Optional<AppUser> findAppUserByFirebaseAuthUid(String firebaseUid);
}
