package com.example.diplomproject.service;


import com.example.diplomproject.dto.RegisterReq;

public interface AuthService {
    boolean login(String username, String password);

    boolean register(RegisterReq registerReq);
}
