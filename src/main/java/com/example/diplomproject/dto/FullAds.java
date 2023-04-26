package com.example.diplomproject.dto;
import com.example.diplomproject.annotations.MyAnnotation;
import lombok.Data;

@Data
public class FullAds {
    @MyAnnotation(name = "id объявления")
    private Integer pk;
    @MyAnnotation(name = "фамилия автора объявления")
    private String authorLastName;
    @MyAnnotation(name = "имя автора объявления")
    private String authorFirstName;
    @MyAnnotation(name = "описание объявления")
    private String description;
    @MyAnnotation(name = "логин автора объявления")
    private String email;
    @MyAnnotation(name = "ссылка на картинку объявления")
    private String image;
    @MyAnnotation(name = "телефон автора объявления")
    private String phone;
    @MyAnnotation(name = "цена объявления")
    private Integer price;
    @MyAnnotation(name = "заголовок объявления")
    private String title;
}
