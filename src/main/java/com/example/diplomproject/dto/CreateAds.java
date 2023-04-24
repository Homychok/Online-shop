package com.example.diplomproject.dto;
import com.example.diplomproject.model.Ads;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
//@Getter
//@Setter
public class CreateAds {
    @MyAnnotation(name = "заголовок объявления")
    private String title;
    @MyAnnotation(name = "описание объявления")
    private String description;
    @MyAnnotation(name = "цена объявления")
    private int price;
    public Ads toAds() {
        Ads ads = new Ads();
        ads.setDescription(this.getDescription());
        ads.setPrice(this.getPrice());
        ads.setTitle(this.getTitle());
        return ads;
    }
}
