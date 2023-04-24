package com.example.diplomproject.service.impl;

import com.example.diplomproject.dto.RegisterReq;
import com.example.diplomproject.repository.UserRepository;
import com.example.diplomproject.service.AuthService;
import com.example.diplomproject.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
@Qualifier("UserService")
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
    if (!manager.userExists(username)) {
      return false;
    }
    UserDetails userDetails = manager.loadUserByUsername(username);
    return encoder.matches(password, userDetails.getPassword());
  }

  @Override
  public boolean register(RegisterReq registerReq) {
    if (manager.userExists(registerReq.getUsername())) {
      return false;
    }
    registerReq.setPassword(encoder.encode(registerReq.getPassword()));
    userRepository.save(registerReq.toUser());
    return true;
  }

}
