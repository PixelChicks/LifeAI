package com.lifeAI.LifeAI.services;


import com.lifeAI.LifeAI.model.User;
import com.lifeAI.LifeAI.model.dto.auth.AdminUserDTO;
import com.lifeAI.LifeAI.model.dto.auth.OAuth2UserInfoDTO;
import com.lifeAI.LifeAI.model.dto.auth.PublicUserDTO;
import com.lifeAI.LifeAI.model.dto.auth.RegisterRequest;

import java.util.List;

public interface UserService {
    User createUser(RegisterRequest request);

    User findByEmail(String email);

    List<User> getAllUsers();

    AdminUserDTO getByIdAdmin(Long id);

    AdminUserDTO updateUser(Long id, AdminUserDTO userDTO, PublicUserDTO currentUser);

    void deleteUserById(Long id, PublicUserDTO currentUser);

    User processOAuthUser(OAuth2UserInfoDTO oAuth2User);

    User findById(Long id);
}
