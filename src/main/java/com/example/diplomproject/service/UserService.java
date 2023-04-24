package com.example.diplomproject.service;
import com.example.diplomproject.dto.NewPassword;
import com.example.diplomproject.dto.UserDTO;
import com.example.diplomproject.exception.IncorrectArgumentException;
import com.example.diplomproject.exception.UserNotFoundException;
import com.example.diplomproject.model.Avatar;
import com.example.diplomproject.model.User;
import com.example.diplomproject.repository.AvatarRepository;
import com.example.diplomproject.repository.ImageRepository;
import com.example.diplomproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final AvatarRepository avatarRepository;
    private final PasswordEncoder encoder;
    private final CustomUserDetailsService manager;

    public User getUserByUsername(String username) {
       return userRepository.findByUsernameIgnoreCase(username);
    }
    public void updatePassword(NewPassword newPassword, Authentication authentication) {
        User user = getUserByUsername(authentication.getName());
        if (!encoder.matches(newPassword.getCurrentPassword(), user.getPassword())) {
            throw new IncorrectArgumentException();
        }
        user.setPassword(encoder.encode(newPassword.getNewPassword()));
        userRepository.save(user);
    }

    public UserDTO getUser(Authentication authentication){
        User user = userRepository.findByUsernameIgnoreCase(authentication.getName());
        if (user == null) {
            throw new UserNotFoundException();
        }
        return UserDTO.fromDTO(user);
    }

    public UserDTO  updateUser(UserDTO userDTO, Authentication authentication){
        User users = userRepository.findByUsernameIgnoreCase(authentication.getName());
        users.setFirstName(userDTO.getFirstName());
        users.setLastName(userDTO.getLastName());
        users.setPhone(userDTO.getPhone());
        userRepository.save(users);
        return UserDTO.fromDTO(users);
    }

    public String updateUserAvatar(MultipartFile avatar, Authentication authentication) throws IOException{
        try {
            User user = userRepository.findByUsernameIgnoreCase(authentication.getName());
            if (user.getAvatar() != null) {
                avatarRepository.delete(user.getAvatar());
            }
            Avatar avatar1 = new Avatar();
            avatar1.setData(avatar.getBytes());
            avatarRepository.save(avatar1);
            user.setAvatar(avatar1);
            userRepository.save(user);
            return "/ads/me/image/" + avatar1.getId();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void deleteUser(String username){
        userRepository.deleteByUsername(username);
    }
}
