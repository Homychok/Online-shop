package com.example.diplomproject.dto;
import com.example.diplomproject.annotations.MyAnnotation;
import lombok.Data;
@Data
public class CreateAds {
    @MyAnnotation(name = "заголовок объявления")
    private String title;
    @MyAnnotation(name = "описание объявления")
    private String description;
    @MyAnnotation(name = "цена объявления")
    private Integer price;
}
