package com.example.diplomproject.dto;

import com.example.diplomproject.annotations.MyAnnotation;
import lombok.Data;
@Data
public class LoginReq {
    @MyAnnotation(name = "пароль")
    private String password;
    @MyAnnotation(name = "логин")
    private String username;

}
