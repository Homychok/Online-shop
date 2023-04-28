package com.example.diplomproject.service;

import com.example.diplomproject.dto.CustomUserDetails;
import com.example.diplomproject.exception.UserNotFoundException;
import com.example.diplomproject.mapper.UserMapper;
import com.example.diplomproject.model.User;
import com.example.diplomproject.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
@Service
//@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsManager {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    public CustomUserDetailsService(PasswordEncoder encoder, UserRepository userRepository) {
        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(UserDetails user) {
        User saveUser = UserMapper.toUser((CustomUserDetails) user);
        saveUser.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(saveUser);

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
        ChecksMethods.checkForLogin(username);
        return UserMapper.toCustomUserDetails(userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(UserNotFoundException::new));
    }

}
