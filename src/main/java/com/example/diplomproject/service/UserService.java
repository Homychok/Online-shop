package com.example.diplomproject.service;
import com.example.diplomproject.dto.NewPassword;
import com.example.diplomproject.dto.UserDTO;
import com.example.diplomproject.exception.ImageNotFoundException;
import com.example.diplomproject.exception.IncorrectArgumentException;
import com.example.diplomproject.exception.UserNotFoundException;
import com.example.diplomproject.mapper.UserMapper;
//import com.example.diplomproject.model.Avatar;
import com.example.diplomproject.model.User;
//import com.example.diplomproject.repository.AvatarRepository;
//import com.example.diplomproject.repository.ImageRepository;
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
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public void updatePassword(NewPassword newPassword, Authentication authentication) {
        ChecksMethods.checkForChangeParameter(newPassword);
        User user = userRepository.findByUsernameIgnoreCase(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
        if (!encoder.matches(newPassword.getCurrentPassword(), user.getPassword())) {
            throw new IncorrectArgumentException();
        }
        user.setPassword(encoder.encode(newPassword.getNewPassword()));
        userRepository.save(user);
    }

    public UserDTO getUserByUsername(Authentication authentication){
        return UserMapper.toDTO(userRepository.findByUsernameIgnoreCase(authentication.getName())
                .orElseThrow(UserNotFoundException::new));
    }

    public UserDTO updateUser(UserDTO userDTO){
        User user = userRepository.findById(userDTO.getId()).orElseThrow(UserNotFoundException::new);
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhone(userDTO.getPhone());
        userRepository.save(user);
        return userDTO;
    }

    public boolean updateUserAvatar(Authentication authentication, MultipartFile avatar) throws IOException{

        User user = userRepository.findByUsernameIgnoreCase(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
        user.setImage(ChecksMethods.checkForChangeParameter(avatar).getBytes());
        userRepository.save(user);
        return true;
    }
    public void deleteUser(String username){
        userRepository.deleteByUsername(username);
    }
    public byte[] showAvatarOnId(Integer id) {
        return userRepository.findById(id).orElseThrow(ImageNotFoundException::new).getImage();
    }
}
