package com.example.diplomproject.dto;

import lombok.Data;

@Data
public class LoginReq {
    @MyAnnotation(name = "пароль")
    private String password;
    @MyAnnotation(name = "логин")
    private String username;

}
