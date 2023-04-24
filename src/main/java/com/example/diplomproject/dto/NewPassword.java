package com.example.diplomproject.dto;
import com.example.diplomproject.annotations.MyAnnotation;
import lombok.Data;

@Data
public class NewPassword {
    @MyAnnotation(name = "текущий пароль")
    private String currentPassword;
    @MyAnnotation(name = "новый пароль")
    private String newPassword;

}
