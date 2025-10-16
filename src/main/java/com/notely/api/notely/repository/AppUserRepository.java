package com.notely.api.notely.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notely.api.notely.entity.AppUser;

@Repository
public interface AppUserRepository  extends JpaRepository<AppUser, Long>{}
