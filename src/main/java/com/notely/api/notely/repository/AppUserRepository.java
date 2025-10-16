package com.notely.api.notely.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.notely.api.notely.entity.AppUser;

public interface AppUserRepository  extends JpaRepository<AppUser, Long>{}
