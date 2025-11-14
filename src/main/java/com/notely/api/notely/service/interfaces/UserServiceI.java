package com.notely.api.notely.service.interfaces;

import java.util.List;

import com.notely.api.notely.dto.AppUserDTO;

public interface UserServiceI {
    List<AppUserDTO> getAllActiveUsers();
    AppUserDTO getCurrentUser();
}

