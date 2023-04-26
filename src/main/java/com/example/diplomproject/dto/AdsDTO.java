package com.example.diplomproject.dto;
import com.example.diplomproject.annotations.MyAnnotation;
import lombok.Data;
@Data
public class AdsDTO {
    @MyAnnotation(name = "id автора объявления")
    private Integer author;
@MyAnnotation(name = "ссылка на картинку объявления")
private String image;
@MyAnnotation(name = "id объявления")
private Integer pk;
    @MyAnnotation(name = "цена объявления")
    private Integer price;
    @MyAnnotation(name = "заголовок объявления")
    private String title;
}
