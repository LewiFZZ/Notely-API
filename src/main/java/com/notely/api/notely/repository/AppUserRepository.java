package com.notely.api.notely.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.notely.api.notely.entity.AppUser;


@Repository
public interface AppUserRepository  extends JpaRepository<AppUser, Long>{
    // Using native query with actual column name to avoid property resolution issues
    @Query(value = "SELECT * FROM app_user WHERE firebase_auth_uid = :firebaseUid", nativeQuery = true)
    Optional<AppUser> findByFirebaseUid(@Param("firebaseUid") String firebaseUid);
    
    // Find all active users
    List<AppUser> findByIsActiveTrue();
}
