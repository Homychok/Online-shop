package com.example.diplomproject.service.impl;

import com.example.diplomproject.dto.RegisterReq;
import com.example.diplomproject.enums.Role;
import com.example.diplomproject.exception.IncorrectArgumentException;
import com.example.diplomproject.exception.UserNotFoundException;
import com.example.diplomproject.mapper.UserMapper;
import com.example.diplomproject.model.User;
import com.example.diplomproject.repository.UserRepository;
import com.example.diplomproject.service.AuthService;
import com.example.diplomproject.service.ChecksMethods;
import com.example.diplomproject.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

  private final CustomUserDetailsService manager;
  private final PasswordEncoder encoder;
  private final UserRepository userRepository;

  public AuthServiceImpl(CustomUserDetailsService manager, PasswordEncoder encoder, UserRepository userRepository) {
    this.manager = manager;
    this.encoder = encoder;
    this.userRepository = userRepository;
  }

  @Override
  public boolean login(String username, String password) {
    ChecksMethods.checkForLogin(password);
    if (!manager.userExists(ChecksMethods.checkForLogin(username))) {
      throw new UserNotFoundException();
    }
    UserDetails userDetails = manager.loadUserByUsername(username);
    return encoder.matches(password, userDetails.getPassword());
  }

  @Override
  public boolean register(RegisterReq registerReq) {
    if (manager.userExists(ChecksMethods.checkForChangeParameter(registerReq).getUsername())) {
      return false;
    }
    registerReq.setPassword(ChecksMethods.checkValidatePassword(registerReq.getPassword()));
    registerReq.setUsername(ChecksMethods.checkForEmail(registerReq.getUsername()));
    registerReq.setPhone(registerReq.getPhone());
    User user = UserMapper.fromRegister(registerReq);
    user.setRole(Role.USER);
    manager.createUser(UserMapper.toCustomUserDetails(user));
    return true;
  }

}
