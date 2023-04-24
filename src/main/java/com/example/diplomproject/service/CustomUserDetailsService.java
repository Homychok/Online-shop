package com.example.diplomproject.service;

import com.example.diplomproject.dto.RegisterReq;
import com.example.diplomproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsManager {

    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;

//    public CustomUserDetailsService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
    @Override
    public void createUser(UserDetails user) {

    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        return RegisterReq.fromRegisterReq(
                userRepository.findByUsernameIgnoreCase(username));
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<User> optionalUser = userRepository.findByUsernameIgnoreCase(username);
//
//        if (optionalUser.isPresent()) {
//            User user = optionalUser.get();
//            return new CustomUserDetails(user);
//        } else {
//            throw new UsernameNotFoundException("User not found with username: " + username);
//        }
//    }
//
//    public void createUser(RegisterReq registerReq) {
//        if (userRepository.findByUsernameIgnoreCase(registerReq.getUsername()).isPresent()) {
//            throw new IncorrectUsernameException();
//        }
//
//            User user = new User();
//        user.setUsername(registerReq.getUsername());
//        user.setPassword(passwordEncoder.encode(registerReq.getPassword()));
//        user.setRole(Role.USER);
//        user.setFirstName(registerReq.getFirstName());
//        user.setLastName(registerReq.getLastName());
//        user.setPhone(registerReq.getPhone());
//        user.setEnabled(true);
//        userRepository.save(user);
//    }

}
