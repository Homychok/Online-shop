package com.example.diplomproject.dto;

import com.example.diplomproject.annotations.MyAnnotation;
import com.example.diplomproject.exception.ImageNotFoundException;
import com.example.diplomproject.model.User;
import lombok.Data;

@Data
public class UserDTO {

    @MyAnnotation(name = "id пользователя")
    private Integer id;
    @MyAnnotation(name = "логин пользователя")
    private String email;
    @MyAnnotation(name = "имя пользователя")
    private String firstName;
    @MyAnnotation(name = "фамилия пользователя")
    private String lastName;
    @MyAnnotation(name = "телефон пользователя")
    private String phone;
    @MyAnnotation(name = "ссылка на аватар пользователя")
    private String image;
    public static UserDTO fromDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPhone(user.getPhone());
        userDTO.setEmail(user.getUsername());
        if (user.getAvatar() == null) {
            userDTO.setImage("No image");
            throw new ImageNotFoundException();
        } else {
            userDTO.setImage("/ads/me/image/" + user.getAvatar().getId());
        }
        return userDTO;
    }
}